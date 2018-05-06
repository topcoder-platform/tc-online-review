package com.topcoder.util.commandline;

import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList ;

/**
 * <p>Provides command line management for applications. A command line can
 *    be thought of as a series of switches (-D, -help) and parameters 
 *    (filename).  Thus "<code>java app -D dir filename</code>" is an example 
 *    of a single-value switch (-D) and a parameter (filename).</p>
 * <p>To simplify the process, all switches must precede all parameters. The 
 *    number of parameters allowed can be limited. Parameters are stored as an 
 *    array of Strings.</p>
 * <p>To use switches, create a Switch for each valid command line switches. To
 *    add validation, use or create a subclass of ArgumentValidator.</p>
 * <p>A simple usage printout is provi using getUsageString(). A more
 *    sophisticated version can be created using getSwitches().</p>
 * <p>The default behavior is to throw ArgumentValidationErrors when
 *    invalid arguments are passed, short-circuiting argument processing.
 *    If ignoreErrors is set, argument processing will continue and
 *    validation errors are stored for later use.<p>
 * <p>Switches may not be used more than once in the same command line.
 *    Instead, a multiple-value switch should be used. For instance, use
 *    "<code>java app -D dir1 dir2</code>" instead of
 *    "<code>java app -D dir1 -D dir2</code>."</p>
 * <p>Examples of single-value switches. Note that the -Ddir switch could be
 *    interpreted as D=dir, Dd=ir, and Ddi=r. If more than one of these values
 *    is a valid switch, then an error will be thrown.
 *    <li><code>java app -D dir</code></li>
 *    <li><code>java app -D=dir</code></li>
 *    <li><code>java app -Ddir</code></li></p>
 * <p>Examples of multiple-value switches. The separator for one-parameter
 *    multiple values can be configured using setMultipleValueSeparator().
 *    <li><code>java app -D=dir1, dir2</code> (comma is separator)</li>
 *    <li><code>java app -D dir1;dir2</code> (semicolon is separator)</li></p>
 * <p>Limitations: This class is not suitable for applications that:
 *    <li>rely upon the order of switches to work correctly,
 *    <li>require intermingling of switches and parameters, or
 *    <li>require multiple instances of the same switch (workaround available).
 * </p>
 * @author snard6
 * @version 1.0
 * @see Switch
 * @see ArgumentValidator
 */

public class CommandLineUtility {
    private String multipleValueSeparator = "," ; // separator for multi-value 
                                                  // switches
    private String switchDesignator = "-" ; // designator for switch start
    private boolean ignoreErrors = false ; // setting for error handling
    private int minimumParameters = 0 ; // minimum params (default = any)
    private int maximumParameters = -1 ; // maximum params (default = any)
    private HashMap switches = null ; // map for switches (name ==> object)
    private List validSwitches = null ; // list of valid switches 
                                        // (produced by parse)
    private List invalidSwitches = null ; // list of invalid switches
                                          // (produced by parse)
    private List parameters = null ; // list for parameters
    private boolean isSeperator = false ; //Used in parse to verify seperators

    /**
     * <p>Creates a CommandLineUtility using the default settings.</p>
     */
    public CommandLineUtility() {
        switches = new HashMap() ;
    }

    /**
     * <p>Creates a CommandLineUtility using the provided settings.</p>
     * @param ignoreErrors true = ignore errors
     *      false (default) = throw exceptions on error
     * @param minimumParameters the minimum number of parameters 
     *      (0 = no minimum)
     * @param maximumParameters the maximum number of parameters 
     *      (-1 = no maximum)
     */
    public CommandLineUtility(boolean ignoreErrors,
                              int minimumParameters,
                              int maximumParameters) {
        switches = new HashMap() ;
        this.ignoreErrors = ignoreErrors;
        this.minimumParameters = minimumParameters ;
        this.maximumParameters = maximumParameters ;
    }

    /**
     * <p>Set the multiple value separator.</p>
     * @param multiValueSeparator the String to use between values
     * @throws NullPointerException if argument is null
     */
    public void setMultipleValueSeparator(String multiValueSeparator)
            throws NullPointerException {
        if (multiValueSeparator == null && !ignoreErrors) {
            throw new NullPointerException();
        }
        this.multipleValueSeparator = multiValueSeparator ;
    }

    /**
      * <p>Set the switch designator. The default is "-".</p>
      * @param switchDesignator the String to use before switches
      * @throws NullPointerException if argument is null
      */
     public void setSwitchDesignator(String switchDesignator) 
            throws NullPointerException {
         if (switchDesignator == null && !ignoreErrors) {
                throw new NullPointerException();
         }
         this.switchDesignator = switchDesignator ;
     }

     /**
      * <p>Returns a simple printout of usage, using switches it has already
      *     added, if the switches don't have any information for usage.
      *     it dosn't append them to the end.</p>
      * @return usage
      */
     public String getUsageString() {
         StringBuffer buf = new StringBuffer() ;
         buf.append("Usage:\n") ;
         if (switches != null && switches.values() != null) {
             for (Iterator it = switches.values().iterator(); it.hasNext(); ) {
                 Switch s = (Switch) it.next();
                 buf.append("\t");
                 buf.append(this.switchDesignator);
                 buf.append(s.getName());
                 buf.append("\t");
                 buf.append(s.getUsage());
                 buf.append("\n");
             }
         }
         return buf.toString() ;
     }

     /**
      * <p>Get list of switches that the user added themself.<p>
      * @return a  of Switch objects
      */
     public Collection getSwitches() {
         return switches.values() ;
     }

     /**
      * <p>Get list of valid switches populated by parse.<p>
      * @return a List of Switch objects that are valid
      */
     public List getValidSwitches() {
         if (validSwitches == null) {
             validSwitches = new ArrayList();
         }
         return validSwitches ;
     }

     /**
      * <p>Get list of invalid switches populated by parse.<p>
      * @return a List of Switch objects that are valid
      */
     public List getInvalidSwitches() {
         if (invalidSwitches == null) {
             invalidSwitches = new ArrayList();
         }
         return invalidSwitches ;
     }

     /**
      * <p>Get a specific switch.<p>
      * @return a Switch object or null if not found 
      *     (may return an invalid switch!)
      */
     public Switch getSwitch(String name) {
         //Check our valid switches first, 
        return (Switch) switches.get(name) ;
     }

     /**
      * <p>Get list of parameters populated by parse.<p>
      * @return a List of parameters
      */
     public List getParameters() {
         if (parameters == null) {
             parameters = new ArrayList();
         }
         return parameters ;
     }

    /**
     * <p>Add a switch to the utility.</p>
     * @param s the switch being added
     * @throws IllegalSwitchException when a switch has a blank name, 
     *    has illegal characters in its name or duplicates an existing switch
     */
    public void addSwitch(Switch s) throws IllegalSwitchException {
        if (minimumParameters != 0 && maximumParameters != -1 
                && minimumParameters > maximumParameters && !ignoreErrors) {
            throw new IllegalSwitchException(s, 
                "Minimum parameters are greater then maximum parameters");
        }
        if (s == null && !ignoreErrors) {
            throw new IllegalSwitchException(s, 
                "null switch can not be added");
        }
        if (s.getName() == null || s.getName().equals("") && !ignoreErrors) {
            throw new IllegalSwitchException(s, 
                "Error in the name of the switch");
        }
        if (switches.containsKey(s.getName()) && !ignoreErrors) {
            throw new IllegalSwitchException(s, 
                "Duplicate value in add Switch");
        }
        if (s.getName().indexOf(multipleValueSeparator) != -1 
                || s.getName().indexOf(switchDesignator) != -1
                && !ignoreErrors) {
            throw new IllegalSwitchException(s, 
                "Illegal character used in name of switch");
        }
        switches.put(s.getName(), s) ;
    }
    
    /**
     * <p>Parse the values provided on the command line.</p>
	 * @param arguments the arguments from the command line
     *     @throws ArgumentValidationException when any of the arguments can not
     *             be validated by a validator
     *     @throws UsageException if it fails for improper usage
     */
    public void parse(String arguments[])
            throws ArgumentValidationException, UsageException {
        //We need to preserve what they've given us, so lets make a copy
        //That we can change all we want
        String[] ourargs = new String[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            ourargs[i] = arguments[i];
        }
        boolean illegal = false; //Verifies it be added to the valid list?
        boolean added_param = false; //We've added a parameter
        boolean need_param = false; //We need to start adding parameters
        //make sure everything has been cleaned up
        validSwitches = new ArrayList();
        invalidSwitches = new ArrayList();
        parameters = new ArrayList();
        //done cleaning
        for (int i = 0; i < ourargs.length; i++) {
            //Since "" strings have to be surrounded by ',' we can safely
            //say that if there is a "" it is a mistake
            if (ourargs[i].equals("")) {
                continue;
            }
            //Does it start with our switch Designator
            if (ourargs[i].startsWith(switchDesignator) 
                    && need_param == false) {
                if (added_param && !ignoreErrors) {
                    throw new UsageException();
                }
                ourargs[i] = ourargs[i].substring(switchDesignator.length());
                //Split about '=', if no '=' exists no action happens
                String[] index_temp = split(ourargs[i], "=");
                //Test to see if splitting it around the '=' worked
                Switch tmp = (Switch) switches.get(index_temp[0]);
                Switch newtmp = null;
                boolean inc = true;
                if (tmp == null) {
                    //The fast way failed, time to run through the slow way
                    for (int k = 0; k < index_temp[0].length(); k++) {
                        //Go through and check character by character if the
                        //switch has melded with the argument
                        newtmp = (Switch) switches.get(
                            index_temp[0].substring(0, k));
                        //It can't have an equals sign so we don't need to worry
                        //about index_temp[1]
                        if (newtmp != null) {
                            //Found a switch the long way, our worked paid off
                            if (tmp != null && !ignoreErrors) {
                                //We've already found one
                                throw new UsageException();
                            }
                            tmp = newtmp;
                            ourargs[i] = index_temp[0].substring(k);
                            inc = false;
                        }
                    }
                }
                if (index_temp.length > 1) {
                    //We broke it around the '=' giving us an extra argument
                    //hanging around.  We replace our original and don't inc i
                    ourargs[i] = index_temp[1];
                }
                else if (inc) {
                    i++;
                    if (i >= ourargs.length && tmp.getMaximumArguments() != 0 
                            && tmp.getMaximumArguments() != -1 
                            && !ignoreErrors) {
                        throw new UsageException();
                    }
                }
                if (tmp != null && i < ourargs.length) {
                    boolean complete = false;//Are we done looking for arguments
                    boolean nomax = false;   //There isn't a max for this switch
                    int max = tmp.getMaximumArguments();
                    int min = tmp.getMinimumArguments();
                    int index = 0;
                    ArrayList t_list = new ArrayList();
                    if (max == -1) {
                        nomax = true;
                    }
                    while (i < ourargs.length && complete == false) {
                        if (ourargs[i].equals("")) {
                            i++;
                            continue;
                        }
                        if (ourargs[i].startsWith(switchDesignator)) {
                            //We've either hit another switch or it's a
                            //negative number
                            if (!isNumber(ourargs[i].substring(1))) {
                                //We hit another swith pull out
                                complete = true;
                                continue;
                            }
                        }
                        if (max == 0) {
                            //We've reached the max number of parameters
                            complete = true;
                            continue;
                        }
                        //If this has a mVS in it... or the next one starts
                        //with an mVS is this one of our multiple value
                        //seperators
                        isSeperator = false;
                        String[] tmp_list;
                        String mvs_string = makeStringFromMVS(ourargs, i);
                        if (isSeperator) {
                            tmp_list = split(mvs_string, 
                                multipleValueSeparator);
                        }
                        else {
                            tmp_list = new String[1];
                            tmp_list[0] = mvs_string;
                        }
                        if (minimumParameters >= ourargs.length - i - 1 
                                && minimumParameters != 0) {
                            if (isSeperator == true || index < min - 1) {
                                //It has a seperator, but we don't have
                                //enough parameters to pass around
                                if (!ignoreErrors) {
                                    throw new UsageException();
                                }
                            }
                            else {
                                //We've already hit our quota we're safe
                                need_param = true;
                                complete = true;
                            }
                        }
                        for (int t = 0; t < tmp_list.length; t++) {
                            if (max != 0 || nomax == true) {
                                t_list.add(tmp_list[t]);
                                max--;
                                index++;
                            }
                            else if (!ignoreErrors) {
                                //max hit 0 but we still have ourargs
                                //that were had seperators in them
                                throw new UsageException();
                            }
                        }
                        i++;
                    }
                    //Add the list to the switch
                    tmp.setValues(t_list, 0, ignoreErrors);
                    //Too little arguments
                    if (index < min && !ignoreErrors) {
                        throw new UsageException();
                    }
                    i--;
                }
                else {
                    //Couldn't find a proper switch must be invalid
                    if (tmp == null && !ignoreErrors) {
                        throw new UsageException();
                    }
                    else if (tmp == null) {
                        continue;
                    }
                    else if (tmp.getMinimumArguments() > 0) {
                        illegal = true;
                    }
                }
                if (tmp != null && !tmp.isValid()) {
                    illegal = true;
                }
                if (illegal == true) {
                    //add another invalid switch to the list
                    if (!invalidSwitches.contains(tmp) || ignoreErrors) {
                        invalidSwitches.add(tmp);
                    }
                    else {
                        throw new UsageException();
                    }
                    illegal = false; //could have legal switches afterwards
                }
                else {
                    //add another valid switch to the list
                    if (!validSwitches.contains(tmp) || ignoreErrors) {
                        validSwitches.add(tmp);
                    }
                    else {
                        throw new UsageException();
                    }
                }
            }
            else {
                //It wasn't a switch, add it as a parameter
                parameters.add(ourargs[i]);
                added_param = true;
            }
        }
        int psize = 0;
        if (parameters != null) {
            psize = parameters.size();
        }
        //Not enough parameters were found
        if (psize < minimumParameters && minimumParameters != 0 
                && !ignoreErrors) {
            throw new UsageException();
        }
        //Too many parameters were left over
        if (psize > maximumParameters && maximumParameters != -1
                && !ignoreErrors) {
            throw new UsageException();
        }
        //Make sure we recieved all that we needed
        Collection all_switches = switches.values();
        if (!ignoreErrors) {
            for (Iterator it = all_switches.iterator(); it.hasNext(); ) {
                Switch tmpsw = (Switch) it.next();
                if (!tmpsw.isValid() && !ignoreErrors) {
                    throw new UsageException();
                }
            }
        }
        return ;
    }
    
    /**
     * <p>Split a string into sub-strings about a certain string.</p>
	 * @param str the string to be split up
         * @param about the string to be split about
     */
    private String[] split(String str, String about) {
        boolean wait_one = false;
        int break_count = countMVS(str, about);
        String[] index_temp = new String[break_count];
        break_count = 0;
        String new_str_tmp = new String("");
        for (int r = 0; r < str.length(); r++) {
            int end_value = (r + about.length() > str.length() - 1) 
                ? str.length()
                : r + about.length();
            int add_one = (end_value + about.length() > str.length() - 1) 
                ? str.length()
                : end_value + about.length();
            if (str.substring(r, end_value).equals(about)) {
                if (str.substring(end_value, add_one).equals(about) 
                        && !wait_one) {
                    wait_one = true;
                    index_temp[break_count] = new_str_tmp;
                    break_count++;
                    new_str_tmp = new String("");
                }
                else if (!wait_one) {
                    index_temp[break_count] = new_str_tmp;
                    break_count++;
                    new_str_tmp = new String("");
                }
                else {
                    wait_one = false;
                }
                r = end_value - 1;
            }
            else {
                new_str_tmp += str.charAt(r);
            }
        }

        if (!str.endsWith(about)) {
            index_temp[break_count] = new_str_tmp;
        }
        return index_temp;
    }
    
    /*
     * Checks to see if the string is a valid number, used to distinguish
     * between negative numbers and switches
     *  @param num the string that is either a negative number or switch
     */

    private boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    /*
     * Goes through and counts how many strings are broken up by the mvs
     *  @str A single string to run through and count
     *  @mvs the multiple-value seperator
     */
    
    private int countMVS(String str, String about) {
        int break_count = 0;
        boolean wait_one = false;
        for (int r = 0; r < str.length(); r++) {
            int end_value = (r + about.length() > str.length() - 1) 
                ? str.length() : r + about.length();
            int add_one = (end_value + about.length() > str.length() - 1) 
                ? str.length()
                : end_value + about.length();
            if (str.substring(r, end_value).equals(about)) {
                if (str.substring(end_value, add_one).equals(about) 
                        && !wait_one) {
                    wait_one = true;
                    break_count++;
                }
                else if (!wait_one) {
                    break_count++;
                }
                else {
                    wait_one = false;
                }
            }
        }
        if (!str.endsWith(about)) {
            break_count++;
        }
        return break_count;
    }
    
    /*
     * Make one large string from the mVS, instead of a list of strings,
     * this value is then passed on to parse
     *  @ourargs the list of strings to concat into a single string
     *  @i the starting position of the args in ourargs
     */
    
    private String makeStringFromMVS(String[] ourargs, int i) {
        if (i >= ourargs.length) {
            return "";
        }
        //Is it in this one?
        if (ourargs[i].indexOf(multipleValueSeparator) != -1) {
            if (ourargs[i].endsWith(multipleValueSeparator)) {
                ourargs[i] += makeStringFromMVS(ourargs, i + 1);
                //Clean it out so we don't use it again
                if (i + 1 < ourargs.length) {
                    ourargs[i + 1] = "";
                }
            }
            isSeperator = true;
        }
        else if (i != (ourargs.length - 1) && ourargs[i + 1].startsWith(
                multipleValueSeparator)) {
            int num_to_add = i + 1;
            if (ourargs[i + 1].equals(multipleValueSeparator)) {
                ourargs[i] += multipleValueSeparator;
                num_to_add = i + 2;
                ourargs[i + 1] = "";
            }
            ourargs[i] += makeStringFromMVS(ourargs, num_to_add);
            if (num_to_add < ourargs.length) {
                ourargs[num_to_add] = "";
            }
            isSeperator = true;
        }
        return ourargs[i];
    }
}