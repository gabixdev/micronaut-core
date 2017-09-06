/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.web.router;

import org.particleframework.core.naming.conventions.PropertyConvention;
import org.particleframework.core.naming.conventions.TypeConvention;
import org.particleframework.stereotype.Controller;

import java.util.List;

import static org.particleframework.core.naming.conventions.MethodConvention.*;
/**
 *
 * <p>An interface for classes capable of building HTTP routing information.</p>
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public interface RouteBuilder {
    /**
     * Used to signify to the route that the ID of the resource is used
     */
    PropertyConvention ID = PropertyConvention.ID;

    /**
     * @return Obtain a list of constructed routes
     */
    List<Route> getConstructedRoutes();
    /**
     * @return The URI naming strategy
     */
    UriNamingStrategy getUriNamingStrategy();

    /**
     * <p>Builds the necessary mappings to treat the given class as a REST endpoint. </p>
     *
     * <p>For example given a class called BookController the following routes will be produced:</p>
     *
     * <pre>{@code
     *     GET "/book"
     *     GET "/book{/id}"
     *     POST "/book"
     *     PUT "/book{/id}"
     *     PATCH "/book{/id}"
     *     DELETE "/book{/id}"
     * }</pre>
     *
     * <p>By default it is assumed the accepted and returned content type is {@link org.particleframework.http.MediaType#APPLICATION_JSON_TYPE}.</p>
     *
     * @param cls The class
     * @return The {@link ResourceRoute}
     */
    ResourceRoute resources(Class cls);

    /**
     * <p>Builds the necessary mappings to treat the given instance as a REST endpoint </p>
     *
     * @param instance The instance
     * @see #resources(Class)
     * @return The {@link ResourceRoute}
     */
    default ResourceRoute resources(Object instance) {
        return resources(instance.getClass());
    }

    /**
     * <p>Builds the necessary mappings to treat the given class as a singular REST endpoint. </p>
     *
     * <p>For example given a class called BookController the following routes will be produced:</p>
     *
     * <pre>{@code
     *     GET "/book"
     *     POST "/book"
     *     PUT "/book"
     *     PATCH "/book"
     *     DELETE "/book"
     * }</pre>
     *
     * <p>By default it is assumed the accepted and returned content type is {@link org.particleframework.http.MediaType#APPLICATION_JSON_TYPE}.</p>
     *
     * @param cls The class
     * @return The {@link ResourceRoute}
     */
    ResourceRoute single(Class cls);

    /**
     * <p>Builds the necessary mappings to treat the given instance as a singular REST endpoint </p>
     *
     * @param instance The instance
     * @see #single(Class)
     * @return The {@link ResourceRoute}
     */
    default ResourceRoute single(Object instance) {
        return single(instance.getClass());
    }

    /**
     * Register a route to handle the returned status code
     *
     * @param code The status code
     * @param instance The instance
     * @param method The method
     * @return The route
     */
    default Route status(int code, Object instance, String method) {
        return status(code, instance.getClass(), method);
    }

    /**
     * Register a route to handle the returned status code
     *
     * @param code The status code
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route status(int code, Class type, String method);

    /**
     * Register a route to handle the error
     *
     * @param error The error
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route error(Class<? extends Throwable> error, Class type, String method);

    /**
     * Register a route to handle the error
     *
     * @param error The error
     * @param instance The instance
     * @param method The method
     * @return The route
     */
    default Route error(Class<? extends Throwable> error, Object instance, String method) {
        return error(error, instance.getClass(), method);
    }

    /**
     * Route the specified URI to the specified target for an HTTP GET. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route GET(String uri, Object target) {
        return GET(uri, target, INDEX.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route GET(Object target) {
        Class<?> type = target.getClass();
        return GET( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route GET(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return GET(getUriNamingStrategy().resolveUri(type, id), target, SHOW.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route GET(Class type) {
        return GET(getUriNamingStrategy().resolveUri(type), type, INDEX.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route GET(Class type, PropertyConvention id) {
        return GET(getUriNamingStrategy().resolveUri(type, id), type, SHOW.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route GET(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route GET(String uri, Class<?> type, String method, Class...parameterTypes);

    /**
     * Route the specified URI to the specified target for an HTTP POST. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route POST(String uri, Object target, Class...parameterTypes) {
        return POST(uri, target, SAVE.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route POST(Object target) {
        Class<?> type = target.getClass();
        return POST( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route POST(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return POST(getUriNamingStrategy().resolveUri(type, id), target, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route POST(Class type) {
        return POST(getUriNamingStrategy().resolveUri(type), type, SAVE.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route POST(Class type, PropertyConvention id) {
        return POST(getUriNamingStrategy().resolveUri(type, id), type, UPDATE.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route POST(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route POST(String uri, Class type, String method, Class...parameterTypes);

    /**
     * Route the specified URI to the specified target for an HTTP PUT. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route PUT(String uri, Object target) {
        return PUT(uri, target, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route PUT(Object target) {
        Class<?> type = target.getClass();
        return PUT( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route PUT(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return PUT(getUriNamingStrategy().resolveUri(type, id), target, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route PUT(Class type) {
        return PUT(getUriNamingStrategy().resolveUri(type), type, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route PUT(Class type, PropertyConvention id) {
        return PUT(getUriNamingStrategy().resolveUri(type, id), type, UPDATE.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route PUT(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route PUT(String uri, Class type, String method, Class...parameterTypes);

    /**
     * Route the specified URI to the specified target for an HTTP PATCH. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route PATCH(String uri, Object target) {
        return PATCH(uri, target, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route PATCH(Object target) {
        Class<?> type = target.getClass();
        return PATCH( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route PATCH(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return PATCH(getUriNamingStrategy().resolveUri(type, id), target, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route PATCH(Class type) {
        return PATCH(getUriNamingStrategy().resolveUri(type), type, UPDATE.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route PATCH(Class type, PropertyConvention id) {
        return PATCH(getUriNamingStrategy().resolveUri(type, id), type, UPDATE.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route PATCH(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route PATCH(String uri, Class type, String method, Class...parameterTypes);

    /**
     * Route the specified URI to the specified target for an HTTP DELETE. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route DELETE(String uri, Object target) {
        return DELETE(uri, target, DELETE.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route DELETE(Object target) {
        Class<?> type = target.getClass();
        return DELETE( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route DELETE(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return DELETE(getUriNamingStrategy().resolveUri(type, id), target, DELETE.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route DELETE(Class type) {
        return DELETE(getUriNamingStrategy().resolveUri(type), type, DELETE.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route DELETE(Class type, PropertyConvention id) {
        return DELETE(getUriNamingStrategy().resolveUri(type, id), type, DELETE.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route DELETE(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route DELETE(String uri, Class type, String method, Class...parameterTypes);

    /**
     * Route the specified URI to the specified target for an HTTP OPTIONS. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route OPTIONS(String uri, Object target) {
        return OPTIONS(uri, target, OPTIONS.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route OPTIONS(Object target) {
        Class<?> type = target.getClass();
        return OPTIONS( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route OPTIONS(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return OPTIONS(getUriNamingStrategy().resolveUri(type, id), target, OPTIONS.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route OPTIONS(Class type) {
        return OPTIONS(getUriNamingStrategy().resolveUri(type), type, OPTIONS.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route OPTIONS(Class type, PropertyConvention id) {
        return OPTIONS(getUriNamingStrategy().resolveUri(type, id), type, OPTIONS.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route OPTIONS(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route OPTIONS(String uri, Class type, String method, Class...parameterTypes);

    /**
     * Route the specified URI to the specified target for an HTTP GET. Since the method to execute is not
     * specified "index" is used by default.
     *
     * @param uri The URI
     * @param target The target object
     * @return The route
     */
    default Route HEAD(String uri, Object target) {
        return HEAD(uri, target, HEAD.methodName());
    }

    /**
     * <p>Route to the specified object. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route HEAD(Object target) {
        Class<?> type = target.getClass();
        return HEAD( getUriNamingStrategy().resolveUri(type), target );
    }

    /**
     * <p>Route to the specified object and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param target The object
     * @return The route
     */
    default Route HEAD(Object target, PropertyConvention id) {
        Class<?> type = target.getClass();
        return HEAD(getUriNamingStrategy().resolveUri(type, id), target, HEAD.methodName());
    }

    /**
     * <p>Route to the specified class. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route HEAD(Class type) {
        return HEAD(getUriNamingStrategy().resolveUri(type), type, HEAD.methodName());
    }

    /**
     * <p>Route to the specified class and ID. The URI route is built by the configured {@link UriNamingStrategy}</p>
     *
     * @param type The class
     * @return The route
     */
    default Route HEAD(Class type, PropertyConvention id) {
        return HEAD(getUriNamingStrategy().resolveUri(type, id), type, HEAD.methodName());
    }

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param target The target
     * @param method The method
     * @return The route
     */
    Route HEAD(String uri, Object target, String method, Class...parameterTypes);

    /**
     * <p>Route the specified URI template to the specified target.</p>
     *
     * <p>The number of variables in the template should match the number of method arguments</p>
     *
     * @param uri The URI
     * @param type The type
     * @param method The method
     * @return The route
     */
    Route HEAD(String uri, Class type, String method, Class...parameterTypes);

    /**
     * <p>A URI naming strategy is used to dictate the default name to use when building a URI for a class</p>
     *
     * <p>The default strategy is as follows:</p>
     *
     * <ul>
     *     <li>{@link #resolveUri(Class)} - Where type is <code>example.BookController</code> value is <code>/book</code></li>
     *     <li>{@link #resolveUri(Class, PropertyConvention)} - Where type is <code>example.BookController</code> value is <code>/book{/id}</code></li>
     * </ul>
     *
     * <p>Implementers can override to provide other strategies such as pluralization etc.</p>
     */
    interface UriNamingStrategy {
        /**
         * Resolve the URI to use for the given type
         *
         * @param type The type
         * @return The URI to use
         */
        default String resolveUri(Class<?> type) {
            Controller annotation = type.getAnnotation(Controller.class);
            String uri = annotation != null ? annotation.value() : null;
            if(uri != null) {
                int len = uri.length();
                if(len == 1 && uri.charAt(0) == '/' ) {
                    return "";
                }
                if(len > 0) {
                    return uri;
                }
            }
            return '/' + TypeConvention.CONTROLLER.asPropertyName(type);
        }

        /**
         * Resolve the URI to use for the given type and route id
         *
         * @param type The type
         * @param id the route id
         * @return The URI to use
         */
        default String resolveUri(Class type, PropertyConvention id) {
            return resolveUri(type) + "{/" + id.lowerCaseName() + "}";
        }
    }

}
