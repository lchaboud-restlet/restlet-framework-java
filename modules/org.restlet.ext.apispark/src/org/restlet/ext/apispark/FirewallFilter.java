/**
 * Copyright 2005-2014 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet S.A.S.
 */

package org.restlet.ext.apispark;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.ext.apispark.internal.firewall.rule.FirewallRule;
import org.restlet.routing.Filter;

/**
 * Filter that controls the incoming requests by applying a set of rules.
 * 
 * @author Guillaume Blondeau
 */
public class FirewallFilter extends Filter {

    /** The list of associated {@link FirewallRule}. */
    private List<FirewallRule> rules;

    /**
     * Constructor.
     */
    public FirewallFilter() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     */
    public FirewallFilter(Context context) {
        this(context, null);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            The context.
     * @param next
     *            The next Restlet.
     */
    public FirewallFilter(Context context, Restlet next) {
        super(context, next);
        rules = new ArrayList<FirewallRule>();
    }

    /**
     * Adds a rule to the firewall.
     * 
     * @param rule
     *            The rule to add.
     */
    public void add(FirewallRule rule) {
        rules.add(rule);
    }

    /**
     * Invokes each {@link FirewallRule#afterHandle(Request, Response)} method.
     */
    @Override
    protected void afterHandle(Request request, Response response) {
        for (FirewallRule rule : rules) {
            rule.afterHandle(request, response);
        }
    }

    /**
     * Applies each rules to the incoming request.
     */
    @Override
    public int beforeHandle(Request request, Response response) {
        int result = Filter.SKIP;
        for (FirewallRule rule : rules) {
            int value = rule.beforeHandle(request, response);
            if (value != Filter.CONTINUE) {
                return value;
            }
            result = value;
        }
        return result;
    }

}
