package com.opensymphony.able.stripes;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.DispatcherHelper;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesServletException;
import net.sourceforge.stripes.validation.BooleanTypeConverter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

public class AbleActionBeanFilter implements Filter {
    public static final String RUN_CUSTOM_VALIDATION_WHEN_ERRORS =
            "Validation.InvokeValidateWhenErrorsExist";

    private Boolean alwaysInvokeValidate;
    private FilterConfig filterConfig;


    public void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // It sucks that we have to do this here (in the request cycle), but there doesn't
        // seem to be a good way to get at the Configuration from the Filter in init()
        doOneTimeConfiguration();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        if ("/".equals(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        Configuration config = StripesFilter.getConfiguration();
        ActionBeanContext context = config.getActionBeanContextFactory().getContextInstance(request, response);
        try {
            // we look up the bean name to see if we should pass the request through or not
            StripesFilter.getConfiguration().getActionResolver().getActionBean(context);
        } catch (StripesServletException e) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        ///////////////////////////////////////////////////////////////////////
        // Here beings the reall processing of the request!
        ///////////////////////////////////////////////////////////////////////
        PageContext pageContext = null;

        try {
            context.setServletContext(filterConfig.getServletContext());

            // Then setup the ExecutionContext that we'll use to process this request
            final ExecutionContext ctx = new ExecutionContext();
            ctx.setInterceptors(config.getInterceptors(LifecycleStage.ActionBeanResolution));
            ctx.setLifecycleStage(LifecycleStage.ActionBeanResolution);
            ctx.setActionBeanContext(context);

            // It's unclear whether this usage of the JspFactory will work in all containers. It looks
            // like it should, but still, we should be careful not to screw up regular request
            // processing if it should fail. Why do we do this?  So we can have a container-agnostic
            // way of getting an ExpressionEvaluator to do expression based validation
            try {
                ActionBeanContext abc = ctx.getActionBeanContext();
                pageContext = JspFactory.getDefaultFactory().getPageContext(AbleServletHelper.servlet, // the servlet inst
                        abc.getRequest(), // req
                        abc.getResponse(), // res
                        null,   // error page url
                        true,   // need session
                        abc.getResponse().getBufferSize(),
                        true); // autoflush
                DispatcherHelper.setPageContext(pageContext);
            }
            catch (Exception e) {
                // Don't even log this, this failure gets reported if action beans actually
                // try and make use of expression validation, otherwise this is just noise
            }

            // Resolve the ActionBean, and if an interceptor returns a resolution, bail now
            Resolution resolution = resolveActionBean(ctx);
            saveActionBean(request);

            if (resolution == null) {
                resolution = resolveHandler(ctx);

                if (resolution == null) {
                    // Then run binding and validation
                    resolution = doBindingAndValidation(ctx);

                    if (resolution == null) {
                        // Then continue on to custom validation
                        resolution = doCustomValidation(ctx);

                        if (resolution == null) {
                            // And then validation error handling
                            resolution = handleValidationErrors(ctx);

                            if (resolution == null) {
                                // And finally(ish) invoking of the event handler
                                resolution = invokeEventHandler(ctx);

                                // If the event produced errors, fill them in
                                DispatcherHelper.fillInValidationErrors(ctx);
                            }
                        }
                    }
                }
            }

            // Whatever stage it came from, execute the resolution
            if (resolution != null) {
                executeResolution(ctx, resolution);
            }
        }
        catch (ServletException se) {
            throw se;
        }
        catch (RuntimeException re) {
            throw re;
        }
        catch (InvocationTargetException ite) {
            if (ite.getTargetException() instanceof ServletException) {
                throw (ServletException) ite.getTargetException();
            } else if (ite.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) ite.getTargetException();
            } else {
                throw new StripesServletException
                        ("ActionBean execution threw an exception.", ite.getTargetException());
            }
        }
        catch (Exception e) {
            throw new StripesServletException("Exception encountered processing request.", e);
        }
        finally {
            // Make sure to release the page context
            if (pageContext != null) {
                JspFactory.getDefaultFactory().releasePageContext(pageContext);
                DispatcherHelper.setPageContext(null);
            }
            restoreActionBean(request);
        }
    }

    public void destroy() {
    }

    /**
     * Responsible for resolving the ActionBean for the current request. Delegates to
     * {@link DispatcherHelper#resolveActionBean(ExecutionContext)}.
     */
    protected Resolution resolveActionBean(ExecutionContext ctx) throws Exception {
        return DispatcherHelper.resolveActionBean(ctx);
    }

    /**
     * Responsible for resolving the event handler method for the current request. Delegates to
     * {@link DispatcherHelper#resolveHandler(ExecutionContext)}.
     */
    protected Resolution resolveHandler(ExecutionContext ctx) throws Exception {
        return DispatcherHelper.resolveHandler(ctx);
    }

    /**
     * Responsible for executing binding and validation for the current request. Delegates to
     * {@link DispatcherHelper#doBindingAndValidation(ExecutionContext, boolean)}.
     */
    protected Resolution doBindingAndValidation(ExecutionContext ctx) throws Exception {
        return DispatcherHelper.doBindingAndValidation(ctx, true);
    }

    /**
     * Responsible for executing custom validation methods for the current request. Delegates to
     * {@link DispatcherHelper#doCustomValidation(ExecutionContext, boolean)}.
     */
    protected Resolution doCustomValidation(ExecutionContext ctx) throws Exception {
        return DispatcherHelper.doCustomValidation(ctx, alwaysInvokeValidate);
    }

    /**
     * Responsible for handling any validation errors that arise during validation. Delegates to
     * {@link DispatcherHelper#handleValidationErrors(ExecutionContext)}.
     */
    protected Resolution handleValidationErrors(ExecutionContext ctx) throws Exception {
        return DispatcherHelper.handleValidationErrors(ctx);
    }

    /**
     * Responsible for invoking the event handler if no validation errors occur. Delegates to
     * {@link DispatcherHelper#invokeEventHandler(ExecutionContext)}.
     */
    protected Resolution invokeEventHandler(ExecutionContext ctx) throws Exception {
        return DispatcherHelper.invokeEventHandler(ctx);
    }

    /**
     * Responsible for executing the Resolution for the current request. Delegates to
     * {@link DispatcherHelper#executeResolution(ExecutionContext, Resolution)}.
     */
    protected void executeResolution(ExecutionContext ctx, Resolution resolution) throws Exception {
        DispatcherHelper.executeResolution(ctx, resolution);
    }

    /**
     * Performs a simple piece of one time configuration that requires access to the
     * Configuration object delivered through the Stripes Filter.
     */
    private void doOneTimeConfiguration() {
        if (alwaysInvokeValidate == null) {
            // Check to see if, in this application, validate() methods should always be run
            // even when validation errors already exist
            String callValidateWhenErrorsExist = StripesFilter.getConfiguration()
                .getBootstrapPropertyResolver().getProperty(RUN_CUSTOM_VALIDATION_WHEN_ERRORS);

            if (callValidateWhenErrorsExist != null) {
                BooleanTypeConverter c = new BooleanTypeConverter();
                this.alwaysInvokeValidate = c.convert(callValidateWhenErrorsExist, Boolean.class, null);
            }
            else {
                this.alwaysInvokeValidate = false; // Default behaviour
            }
        }
    }

    /**
     * Fetches, and lazily creates if required, a Stack in the request to store ActionBeans
     * should the current request involve forwards or includes to other ActionBeans.
     *
     * @param request the current HttpServletRequest
     * @return the Stack if present, or if creation is requested
     */
    protected Stack getActionBeanStack(HttpServletRequest request, boolean create) {
        Stack stack = (Stack) request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN_STACK);
        if (stack == null && create) {
            stack = new Stack();
            request.setAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN_STACK, stack);
        }

        return stack;
    }

    /**
     * Saves the current value of the 'actionBean' attribute in the request so that it
     * can be restored at a later date by calling {@link #restoreActionBean(HttpServletRequest)}.
     * If no ActionBean is currently stored in the request, nothing is changed.
     *
     * @param request the current HttpServletRequest
     */
    protected void saveActionBean(HttpServletRequest request) {
        if (request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN) != null) {
            Stack stack = getActionBeanStack(request, true);
            stack.push(request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN));
        }
    }

    /**
     * Restores the previous value of the 'actionBean' attribute in the request. If no
     * ActionBeans have been saved using {@link #saveActionBean(HttpServletRequest)} then this
     * method has no effect.
     *
     * @param request the current HttpServletRequest
     */
    protected void restoreActionBean(HttpServletRequest request) {
        Stack stack = getActionBeanStack(request, false);
        if (stack != null && !stack.empty()) {
            request.setAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN, stack.pop());
        }
    }
}
