/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id:CastorTestSuiteRunner.java 6775 2007-01-28 20:04:11Z ekuns $
 */
package org.castor.xmlctf;

import java.io.File;


import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Start tests for the Castor Testing Framework. This is the main class for the
 * Castor Testing Framework. It is all driven from here.
 *
 * @author <a href="mailto:gignoux@kernelcenter.org">Sebastien Gignoux</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision:6775 $ $Date: 2005-12-31 08:08:19 -0700 (Sat, 31 Dec 2005) $
 */
public class CastorTestSuiteRunner extends TestCase {

    /**
     * The root directory of the place where all the files generated by the
     * different tests have to be put.
     */
    public static final String TEST_OUTPUT_ROOT = "../xmlctf/build/tests/output/";

    /**
     * Name of the system property storing the root directory of all test cases.
     * @see #_testRoot
     */
    private static final String TEST_ROOT_PROPERTY = "org.exolab.castor.tests.TestRoot";

    /**
     * Root directory of all test cases we will process. We look for test cases
     * recursively starting in this directory.
     */
    private static String _testRoot;

    /**
     * Command line argument that causes the help/options to be displayed.
     */
    private static final String HELP_ARG = "-help";

    /**
     * Command line argument that sets verbose mode true.
     */
    private static final String VERBOSE_ARG = "-verbose";

    /**
     * Command line argument that sets text mode true (no GUI).
     */
    private static final String TEXT_MODE_ARG = "-text";

    /**
     * Command line argument to print or not the stack trace.
     */
    private static final String PRINT_STACK = "-printStack";

    /**
     * Command line argument specifying the seed for the pseudo-random number
     * generator.
     */
    private static final String SEED_ARG = "-seed";

    /**
     * Default constructor that takes a name per test case.
     * @param name test case name
     */
    public CastorTestSuiteRunner(final String name) {
        super(name);
    }

    /**
     * Starts a TestCaseAggregator to collect all the tests in the test root
     * directory and its subdirectories.
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        _testRoot = System.getProperty(TEST_ROOT_PROPERTY);

        if (_testRoot.equals(".") || _testRoot.equals("..")) {
            //-- convert relative directories "." and ".." to a Canonical path
            File tmp = new File(_testRoot);
            try {
                _testRoot = tmp.getCanonicalPath();
            } catch (java.io.IOException iox) {
                iox.printStackTrace();
                System.exit(1);
            }
        } else if (_testRoot.startsWith("./") || _testRoot.startsWith(".\\")) {
            //-- Remove leading ./ or .\ -- URLClassLoader can't handle such file URLs
            _testRoot = _testRoot.substring(2);
        }

        File testRoot = new File(_testRoot);

        if (!testRoot.exists()) {
            System.out.println("\nUnable to locate the root directory for the test cases");
            System.exit(1);
        }
        return new TestCaseAggregator(testRoot, TEST_OUTPUT_ROOT).suite();
    }

    /**
     * Runs the Castor Testing Framework.
     * @param args the standard command-line arguments
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            error(); // Does not return
        }

        boolean text = processArguments(args);

        if (System.getProperty(TEST_ROOT_PROPERTY) == null) {
            error(); // Does not return
        }

        System.out.println("Pseudo-random number generator seed:  " + RandomHelper.getSeed());

        if (text) {
            String[] testCaseName = {CastorTestSuiteRunner.class.getName()};
            junit.textui.TestRunner.main(testCaseName);
        } else {
            //TODO: re-enable if a Maven dependency exists
//            String[] testCaseName = {"-noloading", CastorTestSuiteRunner.class.getName()};
//            junit.swingui.TestRunner.main(testCaseName);
        }
    }

    /**
     * Processes command-line arguments for main() and returns whether or not to
     * use the text or GUI JUnit.
     *
     * @param args command-line arguments from main()
     * @return true if JUnit should be run in text mode, not GUI
     */
    private static boolean processArguments(final String[] args) {
        boolean textModeJUnit = true; // text by default

        for (int i = 0; i < args.length; ++i) {
            String argument = args[i];
            System.out.println("arg: '" + argument + "'");

            if (argument.equals(VERBOSE_ARG)) {
                System.out.println("Verbose on");
                System.setProperty(TestCaseAggregator.VERBOSE_PROPERTY, "true");
            } else if (argument.equals(PRINT_STACK)) {
                System.out.println("Printing stack traces on error on.");
                System.setProperty(TestCaseAggregator.PRINT_STACK_TRACE, "true");
            } else if (argument.equals(TEXT_MODE_ARG)) {
                System.out.println("Running in text mode.");
                textModeJUnit = true;
            } else if (argument.equals(SEED_ARG)) {
                // The next argument should be a number...
                try {
                    long seed = Long.parseLong(args[++i]);
                    RandomHelper.setSeed(seed);
                } catch (NumberFormatException nfe) {
                    System.out.println("Unable to parse the number for the seed");
                    error(); // Does not return
                }
            } else if (argument.equals(HELP_ARG)) {
                usage();
                System.exit(0);
            } else if (System.getProperty(TEST_ROOT_PROPERTY) == null) {
                System.setProperty(TEST_ROOT_PROPERTY, argument);
            } else {
                System.out.println("Unexpected command-line argument: '" + argument + "'");
                error(); // Does not return
            }
        }
        return textModeJUnit;
    }

    /**
     * Print usage and exit with a non-zero return code.
     */
    private static void error() {
        usage();
        System.exit(1);
    }

    /**
     * Print usage.
     */
    private static void usage() {
        System.out.println("Castor Testing Framework ");
        System.out.println("------------------------ ");
        System.out.println("argument: [" + VERBOSE_ARG + "] [" + TEXT_MODE_ARG + "] ["
                           + PRINT_STACK + "] [" + SEED_ARG
                           + " <seed value>] <root test directory or a castor jar test file>");
        System.out.println("   " + HELP_ARG + " : displays this screen.");
        System.out.println("   " + VERBOSE_ARG
                           + " : give detailed execution information for the each test");
        System.out.println("   " + TEXT_MODE_ARG
                           + " : run the test without starting the swing gui");
        System.out.println("   " + PRINT_STACK
                           + " : Print the full stack trace if an exception is thrown");
        System.out.println("   " + SEED_ARG + " <seed value>: "
                           + "set a specific seed for the pseudo-random number generator");
    }

}
