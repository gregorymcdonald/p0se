package P0seRecognizer;

public class Executable {
    public static final String WINDOWS_OS_HEADER = "Windows";
    public static final String MAC_OS_HEADER = "MacOS";
    
    private String compatibleOperatingSystemHeader;
    private String[] execStatement;
    
    public Executable(){
        this(new String[0], "NO COMPATIBLE OPERATING SYSTEM");
    }//constructor: default
    
    public Executable(String[] execStatement, String compatibleOperatingSystemHeader){
        this.compatibleOperatingSystemHeader = compatibleOperatingSystemHeader;
        this.execStatement = execStatement;
    }//constructor: all fields initiated
    
    public boolean execute(){
        String operatingSystemName = System.getProperty("os.name");
        if(operatingSystemName.startsWith(compatibleOperatingSystemHeader)){
            Runtime rt = Runtime.getRuntime();
            try{
                rt.exec(execStatement);
                return true;
            }
            catch(Exception e){
                System.err.println("Failed to execute statement: Exception generated");
                e.printStackTrace();
                return false;
            }
        }//if: running on a compatible operating system
        else{
            System.err.println("Failed to execute statement: Incompatible OS detected");
            return false;
        }//else: not running on a compatible operating system
    }//method: execute
    
    /* ***** ACCESSORS AND MUTATORS ***** */
    public String getCompatibleOperatingSystemHeader(){
        return compatibleOperatingSystemHeader;
    }//method: getCompatibleOperatingSystemHeader
    
    public void setCompatibleOperatingSystemHeader(String compatibleOperatingSystemHeader){
        this.compatibleOperatingSystemHeader = compatibleOperatingSystemHeader;
    }//method: setCompatibleOperatingSystemHeader
    
    public String[] getExecutionStatement(){
        return execStatement;
    }//method: getExecutionStatement
    
    public void setExecutionStatement(String[] executionStatement){
        this.execStatement = executionStatement;
    }//method: getExecutionStatement
}//class: Executable
