package Model;
public class TechniqueModel {
    public static final String TECHNIQUE_CRDP_SCRIPT1 = "#include \"windows.h\"";
    public static final String TECHNIQUE_CRDP_SCRIPT2 = """
            BOOL HasDegubPort = FALSE;
            if (CheckRemoteDebuggerPresent(GetCurrentProcess(), &HasDegubPort) && HasDegubPort) {
                printf("Debugger detected");
                ExitProcess(0);
            }""".indent(4);
    public static final String TECHNIQUE_PTDAW_MAC_SCRIPT1 = """
            int deny_attach_successful = 0;
            void sigsegv_handler(int sig) {
            \tprintf("sigsegv_handler: %i\\n", sig);
            \tdeny_attach_successful = 1;
            }""";
    public static final String TECHNIQUE_PTDAW_MAC_SCRIPT2 = """
            \tpid_t pid = getpid();
            \tptrace(PT_DENY_ATTACH, 0, 0, 0);
            \tsignal(SIGSEGV, sigsegv_handler);
            \tptrace(PT_ATTACH, pid, 0, 0);

            \tif (!deny_attach_successful) {
            \t\tprintf("FAILURE\\n");
            \t\treturn 1;
            \t}""";
    public static final String TECHNIQUE_PTDAW_LINUX_SCRIPT =
            "    if (ptrace(PTRACE_TRACEME, 0, 1, 0) != 0 || ptrace(PTRACE_TRACEME, 0, 1, 0) != -1) return 1;";
    public static final String TECHNIQUE_SBD_SCRIPT1 = """
            #define SPC_DEFINE_DBG_SYM(name) asm(#name ": \\n")
            #define SPC_USE_DBG_SYM(name) extern void name(void)""";
    public static final String TECHNIQUE_SBD_SCRIPT2 = "    SPC_DEFINE_DBG_SYM(%s_nodebug);";
    public static final String TECHNIQUE_SBD_SCRIPT3 = "SPC_USE_DBG_SYM(%s_nodebug);";
    public static final String TECHNIQUE_SBD_SCRIPT4 = """
            if (%s){
                printf("Debugger detected");
                return 1;
            }""".indent(4);
    public static final String TECHNIQUE_SBD_SCRIPT5 = "*(volatile unsigned char *)%s_nodebug == 0xCC";
    public enum Technique{
        TECHNIQUE_EW, TECHNIQUE_SRS, TECHNIQUE_HE, TECHNIQUE_SBD, TECHNIQUE_PTDAW, TECHNIQUE_CRDP
    }

    private final boolean[] techniqueSelected;

    public TechniqueModel(){
        this.techniqueSelected = new boolean[Technique.values().length];
    }

    public void setTechniqueSelected(Technique technique, boolean selected){
        techniqueSelected[technique.ordinal()] = selected;
    }

    public boolean getTechniqueSelected(Technique technique){
        return techniqueSelected[technique.ordinal()];
    }
}
