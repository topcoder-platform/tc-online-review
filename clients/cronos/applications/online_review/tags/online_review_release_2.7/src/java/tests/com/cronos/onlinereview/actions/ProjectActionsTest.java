package com.cronos.onlinereview.actions;

import servletunit.struts.CactusStrutsTestCase;

/**
 * This class tests Struts Actions performed by <code>ProjectActions</code> class.
 *
 * @author TCAssemblyTeam
 * @version 1.0
 */
public class ProjectActionsTest extends CactusStrutsTestCase {

    /**
     * This method sets up the environment for the tests defined by current class.
     */
    public void setUp() {
    }

    /**
     * This method cleans up an environment that was previously set up by <code>setUp</code>
     * method.
     *
     * @see #setUp
     */
    public void tearDown() {
    }

    /**
     * This method tests correctness of execution of "List Projects" action when no "scope"
     * parameter was specified.
     */
    public void testListProjectsScopeNone() {
        addRequestParameter("method", "listProjects");
        setRequestPathInfo("/actions/ListProjects");

        actionPerform();

        verifyForward("success");
        verifyForwardPath("/jsp/listProjects.jsp");
        verifyNoActionErrors();
    }

    /**
     * This method tests correctness of execution of "List Projects" action with value "my" for
     * parameter "scope" supplied.
     */
    public void testListProjectsScopeMy() {
        addRequestParameter("scope", "my");
        addRequestParameter("method", "listProjects");
        setRequestPathInfo("/actions/ListProjects");

        actionPerform();

        verifyForward("success");
        verifyForwardPath("/jsp/listProjects.jsp");
        verifyNoActionErrors();
    }

	/**
     * This method tests correctness of execution of "List Projects" action with value "all" for
     * parameter "scope" supplied.
	 */
    public void testListProjectsScopeAll() {
		addRequestParameter("scope", "all");
        addRequestParameter("method", "listProjects");
        setRequestPathInfo("/actions/ListProjects");

        actionPerform();

        verifyForward("success");
        verifyForwardPath("/jsp/listProjects.jsp");
        verifyNoActionErrors();

//        assertEquals("deryl", getSession().getAttribute("authentication"));
	}

    /**
     * This method tests correctness of execution of "List Projects" action with value "inactive"
     * for parameter "scope" supplied.
     */
    public void testListProjectsScopeInactive() {
        addRequestParameter("scope", "inactive");
        addRequestParameter("method", "listProjects");
        setRequestPathInfo("/actions/ListProjects");

        actionPerform();

        verifyForward("success");
        verifyForwardPath("/jsp/listProjects.jsp");
        verifyNoActionErrors();
    }
}
