package judge.remote.provider.vjudge;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import judge.remote.RemoteOjInfo;
import judge.remote.language.LanguageFinder;
import judge.service.JudgeService;
import judge.tool.Handler;

@Component
public class VjudgeLanguageFinder implements LanguageFinder {

    private Map<String,LinkedHashMap<String, String>> ojLanguageMap = new HashMap<>();
    private long lastUpdateTime = 0;
    private final static Logger log = LoggerFactory.getLogger(JudgeService.class);
    
    public VjudgeLanguageFinder() {
        String json = "{\"SGU\":{\"name\":\"SGU\",\"languages\":{\"GNU C (MinGW, GCC 5)\":\"GNU C (MinGW, GCC 5)\",\"GNU CPP (MinGW, GCC 5)\":\"GNU CPP (MinGW, GCC 5)\",\"GNU CPP 14 (MinGW, GCC 5)\":\"GNU CPP 14 (MinGW, GCC 5)\",\"Visual Studio C++ 2010\":\"Visual Studio C++ 2010\",\"C#\":\"C#\",\"Visual Studio C 2010\":\"Visual Studio C 2010\",\"JAVA 7\":\"JAVA 7\",\"Delphi 7.0\":\"Delphi 7.0\"}}," + 
                "\"FZU\":{\"name\":\"FZU\",\"languages\":{\"0\":\"GNU C++\",\"1\":\"GNU C\",\"2\":\"Pascal\",\"3\":\"Java\",\"4\":\"Visual C++\",\"5\":\"Visual C\"}}," + 
                "\"UVA\":{\"name\":\"UVA\",\"languages\":{\"1\":\"ANSI C 5.3.0\",\"2\":\"JAVA 1.8.0\",\"3\":\"C++ 5.3.0\",\"4\":\"PASCAL 3.0.0\",\"5\":\"C++11 5.3.0\",\"6\":\"PYTH3 3.5.1\"}}," + 
                "\"HRBUST\":{\"name\":\"HRBUST\",\"languages\":{\"2\":\"G++\",\"1\":\"GCC\",\"3\":\"JAVA\",\"4\":\"PHP\",\"5\":\"Python2\",\"7\":\"Haskell\"}}," + 
                "\"SCU\":{\"name\":\"SCU\",\"languages\":{\"C++\":\"C++\",\"C\":\"C\",\"Java\":\"Java\",\"Pascal\":\"Pascal\"}}," + 
                "\"51Nod\":{\"name\":\"51Nod\",\"languages\":{\"C\":\"C\",\"C11\":\"C11\",\"CPlus\":\"C++\",\"CPlus11\":\"C++11\",\"VCPlus\":\"Visual C++\",\"CSharp\":\"C#\",\"Java\":\"Java\",\"Python2\":\"Python2\",\"Python3\":\"Python3\",\"PyPy2\":\"PyPy2\",\"PyPy3\":\"PyPy3\",\"Ruby\":\"Ruby\",\"Php\":\"PHP\",\"Haskell\":\"Haskell\",\"Scala\":\"Scala\",\"Javascript\":\"Javascript\",\"Go\":\"Go\",\"OC\":\"Objective-C\",\"Pascal\":\"Pascal\"}}," + 
                "\"TopCoder\":{\"name\":\"TopCoder\",\"languages\":{\"1\":\"Java\",\"3\":\"C++\",\"4\":\"C#\",\"5\":\"VB\",\"6\":\"Python\"}}," + 
                "\"Z_trening\":{\"name\":\"Z_trening\",\"languages\":{\"1\":\"Pascal fpc 3.0.0\",\"2\":\"C gcc 6.3.1\",\"3\":\"C99 gcc 6.3.1\",\"4\":\"C++98 gcc 6.3.1\",\"5\":\"C++11 gcc 6.3.1\",\"6\":\"C++14 gcc 6.3.1\",\"7\":\"Java gcc-gcj 6.3.1\"}}," + 
                "\"HUST\":{\"name\":\"HUST\",\"languages\":{\"0\":\"C\",\"1\":\"C++\",\"2\":\"Pascal\",\"3\":\"Java\"}}," + 
                "\"HackerRank\":{\"name\":\"HackerRank\",\"languages\":{\"c\":\"c\",\"cpp\":\"cpp\",\"java\":\"java\",\"csharp\":\"csharp\",\"php\":\"php\",\"ruby\":\"ruby\",\"python\":\"python\",\"perl\":\"perl\",\"haskell\":\"haskell\",\"clojure\":\"clojure\",\"scala\":\"scala\",\"lua\":\"lua\",\"go\":\"go\",\"javascript\":\"javascript\",\"erlang\":\"erlang\",\"d\":\"d\",\"ocaml\":\"ocaml\",\"pascal\":\"pascal\",\"python3\":\"python3\",\"groovy\":\"groovy\",\"objectivec\":\"objectivec\",\"fsharp\":\"fsharp\",\"visualbasic\":\"visualbasic\",\"lolcode\":\"lolcode\",\"smalltalk\":\"smalltalk\",\"tcl\":\"tcl\",\"whitespace\":\"whitespace\",\"sbcl\":\"sbcl\",\"java8\":\"java8\",\"octave\":\"octave\",\"racket\":\"racket\",\"rust\":\"rust\",\"bash\":\"bash\",\"r\":\"r\",\"swift\":\"swift\",\"fortran\":\"fortran\",\"cpp14\":\"cpp14\",\"coffeescript\":\"coffeescript\",\"ada\":\"ada\",\"pypy\":\"pypy\",\"pypy3\":\"pypy3\",\"julia\":\"julia\",\"elixir\":\"elixir\"}}," + 
                "\"OpenJ_Bailian\":{\"name\":\"OpenJ_Bailian\",\"languages\":{\"G++\":\"G++(4.5)\",\"GCC\":\"GCC(4.5)\",\"Java\":\"Java()\",\"Pascal\":\"Pascal(FreePascal)\"}}," + 
                "\"HDU\":{\"name\":\"HDU\",\"languages\":{\"0\":\"G++\",\"1\":\"GCC\",\"2\":\"C++\",\"3\":\"C\",\"4\":\"Pascal\",\"5\":\"Java\",\"6\":\"C#\"}}," + 
                "\"UESTC_old\":{\"name\":\"UESTC_old\",\"languages\":{}}," + 
                "\"AtCoder\":{\"name\":\"AtCoder\",\"languages\":{\"3003\":\"C++14 (GCC 5.3.0)\",\"3001\":\"Bash (GNU bash v4.3.11)\",\"3002\":\"C (GCC 5.3.0)\",\"3004\":\"C (Clang 3.8.0)\",\"3005\":\"C++14 (Clang 3.8.0)\",\"3006\":\"C# (Mono 4.2.2.30)\",\"3007\":\"Clojure (1.8.0)\",\"3008\":\"Common Lisp (SBCL 1.1.14)\",\"3009\":\"D (DMD64 v2.070.1)\",\"3010\":\"D (LDC 0.17.0)\",\"3011\":\"D (GDC 4.9.3)\",\"3012\":\"Fortran (gfortran v4.8.4)\",\"3013\":\"Go (1.6)\",\"3014\":\"Haskell (GHC 7.10)\",\"3015\":\"Java7 (OpenJDK 1.7.0)\",\"3016\":\"Java8 (OpenJDK 1.8.0)\",\"3017\":\"JavaScript (node.js v5.7)\",\"3018\":\"OCaml (4.02.3)\",\"3019\":\"Pascal (FPC 2.6.2)\",\"3020\":\"Perl (v5.18.2)\",\"3021\":\"PHP (5.6.18)\",\"3022\":\"Python2 (2.7.6)\",\"3023\":\"Python3 (3.4.3)\",\"3024\":\"Ruby (2.3.0)\",\"3025\":\"Scala (2.11.7)\",\"3026\":\"Scheme (Gauche 0.9.3.3)\",\"3027\":\"Text (cat)\",\"3028\":\"Visual Basic (Mono 4.2.2.30)\",\"3501\":\"Objective-C (GCC 5.3.0)\",\"3502\":\"Objective-C (Clang3.7.1)\",\"3503\":\"Swift (swift-2.2-RELEASE)\",\"3504\":\"Rust (1.7.0)\",\"3505\":\"Sed (GNU sed 4.2.2)\",\"3506\":\"Awk (mawk 1.3.3)\",\"3507\":\"Brainfuck (bf 20041219)\",\"3508\":\"Standard ML (MLton 20100608)\",\"3509\":\"PyPy2 (4.0.1)\",\"3510\":\"PyPy3 (2.4.0)\",\"3511\":\"Crystal (0.12.0)\",\"3512\":\"F# (Mono 4.2.2.30)\",\"3513\":\"Unlambda (0.1.3)\",\"3514\":\"Lua (5.3.2)\",\"3515\":\"LuaJIT (2.0.2)\",\"3516\":\"MoonScript (0.4.0)\",\"3517\":\"Ceylon (1.2.1)\",\"3518\":\"Julia (0.4.2)\",\"3519\":\"Octave (4.0.0)\",\"3520\":\"Nim (0.13.0)\",\"3521\":\"TypeScript (1.8.2)\",\"3522\":\"Perl6 (rakudo-star 2016.01)\",\"3523\":\"Kotlin (1.0.0)\",\"3524\":\"PHP7 (7.0.4)\"}}," + 
                "\"HYSBZ\":{\"name\":\"HYSBZ\",\"languages\":{\"0\":\"C\",\"1\":\"C++\",\"2\":\"Pascal\",\"3\":\"Java\",\"4\":\"Ruby\",\"5\":\"Bash\",\"6\":\"Python\"}}," + 
                "\"Gym\":{\"name\":\"Gym\",\"languages\":{\"10\":\"GNU GCC 5.1.0\",\"43\":\"GNU GCC C11 5.1.0\",\"1\":\"GNU G++ 5.1.0\",\"42\":\"GNU G++11 5.1.0\",\"50\":\"GNU G++14 6.2.0\",\"2\":\"Microsoft Visual C++ 2010\",\"9\":\"C# Mono 3.12.1.0\",\"29\":\"MS C# .NET 4.0.30319\",\"28\":\"D DMD32 v2.069.2\",\"32\":\"Go 1.5.2\",\"12\":\"Haskell GHC 7.8.3\",\"36\":\"Java 1.8.0_66\",\"19\":\"OCaml 4.02.1\",\"3\":\"Delphi 7\",\"4\":\"Free Pascal 2.6.4\",\"13\":\"Perl 5.20.1\",\"6\":\"PHP 5.4.42\",\"7\":\"Python 2.7.10\",\"31\":\"Python 3.5.1\",\"40\":\"PyPy 2.7.10 (2.6.1)\",\"41\":\"PyPy 3.2.5 (2.4.0)\",\"8\":\"Ruby 2.0.0p645\",\"49\":\"Rust 1.10\",\"20\":\"Scala 2.11.7\",\"34\":\"JavaScript V8 4.8.0\"}}," + 
                "\"Aizu\":{\"name\":\"Aizu\",\"languages\":{\"C\":\"C\",\"C++\":\"C++\",\"JAVA\":\"JAVA\",\"C++11\":\"C++11\",\"C++14\":\"C++14\",\"C#\":\"C#\",\"D\":\"D\",\"Ruby\":\"Ruby\",\"Python\":\"Python\",\"Python3\":\"Python3\",\"PHP\":\"PHP\",\"JavaScript\":\"JavaScript\",\"Scala\":\"Scala\",\"Haskell\":\"Haskell\",\"OCaml\":\"OCaml\"}}," + 
                "\"SPOJ\":{\"name\":\"SPOJ\",\"languages\":{\"7\":\"Ada95 (gnat 6.3)\",\"59\":\"Any document (no testing)\",\"13\":\"Assembler 32 (nasm 2.12.01)\",\"45\":\"Assembler 32 (gcc 6.3 )\",\"42\":\"Assembler 64 (nasm 2.12.01)\",\"105\":\"AWK (mawk 1.3.3)\",\"104\":\"AWK (gawk 4.1.3)\",\"28\":\"Bash (bash 4.4.5)\",\"110\":\"BC (bc 1.06.95)\",\"12\":\"Branf**k (bff 1.0.6)\",\"81\":\"C (clang 4.0)\",\"11\":\"C (gcc 6.3)\",\"27\":\"C# (gmcs 4.6.2)\",\"1\":\"C++ (gcc 6.3)\",\"41\":\"C++ (g++ 4.3.2)\",\"82\":\"C++14 (clang 4.0)\",\"44\":\"C++14 (gcc 6.3)\",\"34\":\"C99 (gcc 6.3)\",\"14\":\"Clips (clips 6.24)\",\"111\":\"Clojure (clojure 1.8.0)\",\"118\":\"Cobol (opencobol 1.1.0)\",\"91\":\"CoffeeScript (coffee 1.12.2)\",\"31\":\"Common Lisp (sbcl 1.3.13)\",\"32\":\"Common Lisp (clisp 2.49)\",\"102\":\"D (dmd 2.072.2)\",\"84\":\"D (ldc 1.1.0)\",\"20\":\"D (gdc 6.3)\",\"48\":\"Dart (dart 1.21)\",\"96\":\"Elixir (elixir 1.3.3)\",\"36\":\"Erlang (erl 19)\",\"124\":\"F# (mono 4.0.0)\",\"92\":\"Fantom (fantom 1.0.69)\",\"107\":\"Forth (gforth 0.7.3)\",\"5\":\"Fortran (gfortran 6.3)\",\"114\":\"Go (go 1.7.4)\",\"98\":\"Gosu (gosu 1.14.2)\",\"121\":\"Groovy (groovy 2.4.7)\",\"21\":\"Haskell (ghc 8.0.1)\",\"16\":\"Icon (iconc 9.5.1)\",\"9\":\"Intercal (ick 0.3)\",\"24\":\"JAR (JavaSE 6)\",\"10\":\"Java (HotSpot 8u112)\",\"112\":\"JavaScript (SMonkey 24.2.0)\",\"35\":\"JavaScript (rhino 1.7.7)\",\"47\":\"Kotlin (kotlin 1.0.6)\",\"26\":\"Lua (luac 5.3.3)\",\"30\":\"Nemerle (ncc 1.2.0)\",\"25\":\"Nice (nicec 0.9.13)\",\"122\":\"Nim (nim 0.16.0)\",\"56\":\"Node.js (node 7.4.0)\",\"43\":\"Objective-C (gcc 6.3)\",\"83\":\"Objective-C (clang 4.0)\",\"8\":\"Ocaml (ocamlopt 4.01)\",\"22\":\"Pascal (fpc 3.0.0)\",\"2\":\"Pascal (gpc 20070904)\",\"60\":\"PDF (ghostscript 8.62)\",\"3\":\"Perl (perl 5.24.1)\",\"54\":\"Perl (perl 6)\",\"29\":\"PHP (php 7.1.0)\",\"94\":\"Pico Lisp (pico 16.12.8)\",\"19\":\"Pike (pike 8.0)\",\"61\":\"PostScript (ghostscript 8.62)\",\"15\":\"Prolog (swi 7.2.3)\",\"108\":\"Prolog (gnu prolog 1.4.5)\",\"4\":\"Python (cpython 2.7.13)\",\"99\":\"Python (PyPy 2.6.0)\",\"116\":\"Python 3 (python  3.5)\",\"126\":\"Python 3 nbc (python 3.4)\",\"117\":\"R (R 3.3.2)\",\"95\":\"Racket (racket 6.7)\",\"17\":\"Ruby (ruby 2.3.3)\",\"93\":\"Rust (rust 1.14.0)\",\"39\":\"Scala (scala 2.12.1)\",\"33\":\"Scheme (guile 2.0.13)\",\"18\":\"Scheme (stalin 0.3)\",\"97\":\"Scheme (chicken 4.11.0)\",\"46\":\"Sed (sed 4)\",\"23\":\"Smalltalk (gst 3.2.5)\",\"40\":\"SQLite (sqlite 3.16.2)\",\"85\":\"Swift (swift 3.0.2)\",\"38\":\"TCL (tcl 8.6)\",\"62\":\"Text (plain text)\",\"115\":\"Unlambda (unlambda 0.1.4.2)\",\"50\":\"VB.net (mono 4.6.2)\",\"6\":\"Whitespace (wspace 0.3)\"}}," + 
                "\"CodeForces\":{\"name\":\"CodeForces\",\"languages\":{\"1\":\"GNU G++ 5.1.0\",\"10\":\"GNU GCC 5.1.0\",\"12\":\"Haskell GHC 7.8.3\",\"13\":\"Perl 5.20.1\",\"14\":\"ActiveTcl 8.5\",\"15\":\"Io-2008-01-07 (Win32)\",\"17\":\"Pike 7.8\",\"18\":\"Befunge\",\"19\":\"OCaml 4.02.1\",\"2\":\"Microsoft Visual C++ 2010\",\"20\":\"Scala 2.11.7\",\"22\":\"OpenCobol 1.0\",\"25\":\"Factor\",\"26\":\"Secret_171\",\"27\":\"Roco\",\"28\":\"D DMD32 v2.069.2\",\"29\":\"MS C# .NET 4.0.30319\",\"3\":\"Delphi 7\",\"31\":\"Python 3.5.1\",\"32\":\"Go 1.5.2\",\"33\":\"Ada GNAT 4\",\"34\":\"JavaScript V8 4.8.0\",\"36\":\"Java 1.8.0_66\",\"38\":\"Mysterious Language\",\"39\":\"FALSE\",\"4\":\"Free Pascal 2.6.4\",\"40\":\"PyPy 2.7.10 (2.6.1)\",\"41\":\"PyPy 3.2.5 (2.4.0)\",\"42\":\"GNU G++11 5.1.0\",\"43\":\"GNU GCC C11 5.1.0\",\"49\":\"Rust 1.10\",\"50\":\"GNU G++14 6.2.0\",\"6\":\"PHP 5.4.42\",\"7\":\"Python 2.7.10\",\"8\":\"Ruby 2.0.0p645\",\"9\":\"C# Mono 3.12.1.0\"}}," + 
                "\"ACdream\":{\"name\":\"ACdream\",\"languages\":{\"1\":\"C\",\"2\":\"C++\",\"3\":\"Java\"}}," + 
                "\"CSU\":{\"name\":\"CSU\",\"languages\":{\"0\":\"C\",\"1\":\"C++\",\"3\":\"Java\",\"6\":\"Python\"}}," + 
                "\"CodeChef\":{\"name\":\"CodeChef\",\"languages\":{\"7\":\"ADA 95(gnat 4.9.2)\",\"13\":\"Assembler(nasm 2.11.05)\",\"28\":\"Bash(bash-4.3.30)\",\"12\":\"Brainf**k(bff-1.0.5)\",\"11\":\"C(gcc-4.9.2)\",\"34\":\"C99 strict(gcc 4.9.2)\",\"8\":\"Ocaml(ocamlopt 4.01.0)\",\"111\":\"Clojure(clojure 1.7)\",\"14\":\"Clips(clips-6.24)\",\"41\":\"C++(gcc-4.3.2)\",\"1\":\"C++(gcc-4.9.2)\",\"44\":\"C++14(g++ 4.9.2)\",\"27\":\"C#(gmcs 3.10)\",\"20\":\"D(gdc 4.9.2)\",\"5\":\"Fortran(gfortran 4.9.2)\",\"124\":\"F#(fsharp-3.10)\",\"114\":\"Go(1.4)\",\"21\":\"Haskell(ghc-7.6.3)\",\"9\":\"Intercal(ick-0.28-4)\",\"16\":\"Icon(iconc-9.4.3)\",\"10\":\"Java(javac 8)\",\"32\":\"Common Lisp(clisp 2.49)\",\"31\":\"Common Lisp(sbcl-1.0.18)\",\"26\":\"Lua(luac-5.2)\",\"30\":\"Nemerle(ncc-0.9.3)\",\"25\":\"Nice(nice-0.9.6)\",\"56\":\"JavaScript(node.js-0.10.35)\",\"22\":\"Pascal(fpc-2.6.4)\",\"2\":\"Pascal(gpc-20070904)\",\"3\":\"Perl(perl-5.20.1)\",\"29\":\"PHP(php 5.6.4)\",\"19\":\"Pike(pike-7.8)\",\"15\":\"Prolog(swipl-5.6.58)\",\"99\":\"Python(Pypy)\",\"4\":\"Python(python-2.7.9)\",\"116\":\"Python3(python-3.4)\",\"17\":\"Ruby(ruby-1.9.3)\",\"39\":\"Scala(scala 2.11.4)\",\"97\":\"Scheme(chicken)\",\"33\":\"Scheme(guile 2.0.11)\",\"18\":\"Scheme(stalin-0.11)\",\"23\":\"Smalltalk(gst 3.2.4)\",\"38\":\"Tcl(tclsh 8.6)\",\"62\":\"Text(pure text)\",\"6\":\"Whitespace(wspace-0.2)\"}}," + 
                "\"UVALive\":{\"name\":\"UVALive\",\"languages\":{\"1\":\"ANSI C 5.3.0\",\"2\":\"JAVA 1.8.0\",\"3\":\"C++ 5.3.0\",\"4\":\"PASCAL 3.0.0\",\"5\":\"C++11 5.3.0\",\"6\":\"PYTH3 3.5.1\"}}," + 
                "\"OpenJ_POJ\":{\"name\":\"OpenJ_POJ\",\"languages\":{\"G++\":\"G++(4.5)\",\"GCC\":\"GCC(4.5)\",\"Java\":\"Java()\",\"Pascal\":\"Pascal(FreePascal)\"}}," + 
                "\"Kattis\":{\"name\":\"Kattis\",\"languages\":{\"C++\":\"C++\",\"C\":\"C\",\"Java\":\"Java\",\"Pascal\":\"Pascal\",\"Text\":\"Text\",\"Python 2\":\"Python 2\",\"Python 3\":\"Python 3\",\"C#\":\"C#\",\"Go\":\"Go\",\"Objective-C\":\"Objective-C\",\"Haskell\":\"Haskell\",\"Prolog\":\"Prolog\",\"JavaScript\":\"JavaScript\",\"PHP\":\"PHP\",\"Ruby\":\"Ruby\"}}," + 
                "\"POJ\":{\"name\":\"POJ\",\"languages\":{\"0\":\"G++\",\"1\":\"GCC\",\"2\":\"Java\",\"3\":\"Pascal\",\"4\":\"C++\",\"5\":\"C\",\"6\":\"Fortran\"}}," + 
                "\"HihoCoder\":{\"name\":\"HihoCoder\",\"languages\":{\"GCC\":\"GCC\",\"G++\":\"G++\",\"C#\":\"C#\",\"Java\":\"Java\",\"Python2\":\"Python2\"}}," + 
                "\"URAL\":{\"name\":\"URAL\",\"languages\":{\"31\":\"FreePascal 2.6\",\"37\":\"Visual C 2013\",\"38\":\"Visual C++ 2013\",\"25\":\"GCC 4.9\",\"26\":\"G++ 4.9\",\"27\":\"GCC 4.9 C11\",\"28\":\"G++ 4.9 C++11\",\"30\":\"Clang 3.5 C++14\",\"32\":\"Java 1.8\",\"11\":\"Visual C# 2010\",\"15\":\"VB.NET 2010\",\"34\":\"Python 2.7\",\"35\":\"Python 3.4\",\"14\":\"Go 1.3\",\"18\":\"Ruby 1.9\",\"19\":\"Haskell 7.6\",\"33\":\"Scala 2.11\"}}," + 
                "\"HIT\":{\"name\":\"HIT\",\"languages\":{\"C++\":\"C++\",\"C89\":\"C89\",\"Java\":\"Java\",\"Pascal\":\"Pascal\"}}," + 
                "\"LightOJ\":{\"name\":\"LightOJ\",\"languages\":{\"C\":\"C\",\"C++\":\"C++\",\"JAVA\":\"JAVA\",\"PASCAL\":\"PASCAL\"}}," + 
                "\"ZOJ\":{\"name\":\"ZOJ\",\"languages\":{\"1\":\"C (gcc 4.7.2)\",\"2\":\"C++ (g++ 4.7.2)\",\"3\":\"FPC (fpc 2.6.0)\",\"4\":\"Java (java 1.7.0)\",\"5\":\"Python (Python 2.7.3)\",\"6\":\"Perl (Perl 5.14.2)\",\"7\":\"Scheme (Guile 1.8.8)\",\"8\":\"PHP (PHP 5.4.4)\",\"9\":\"C++0x (g++ 4.7.2)\"}}," + 
                "\"NBUT\":{\"name\":\"NBUT\",\"languages\":{\"1\":\"GCC\",\"2\":\"G++\",\"4\":\"FPC\"}}," + 
                "\"EIJudge\":{\"name\":\"EIJudge\",\"languages\":{\"Free Pascal\":\"Free Pascal 1.8.2\",\"GNU C\":\"GNU C 3.3.3\",\"GNU C++\":\"GNU C++ 3.3.3\",\"Haskell\":\"Haskell GC 6.8.2\",\"Java\":\"java 1.5.0\",\"Kylix\":\"Kylix 14.5\",\"Lua\":\"Lua 5.1.3\",\"OCaml\":\"Objective Caml 3.10.2\",\"Perl\":\"Perl 5.8.5\",\"Python\":\"Python 2.1.3\",\"Ruby\":\"Ruby 1.8.6\",\"Scheme\":\"mzScheme 301 Swindle\"}}," + 
                "\"UESTC\":{\"name\":\"UESTC\",\"languages\":{\"1\":\"C\",\"2\":\"C++\",\"3\":\"Java\"}}}";
                
        try {
            updateOjLanguageMap(json);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void updateOjLanguageMap(String json) throws JSONException {
        Map<String,LinkedHashMap<String, String>> newOjLanguageMap = new HashMap<>();
        Map<String,Object> ojs = (Map<String, Object>) JSONUtil.deserialize(json);
        for(String oj : ojs.keySet()) {
            LinkedHashMap<String, String> languageList = new LinkedHashMap<String, String>(
                    (Map<String, String>)((Map<String, Object>)ojs.get(oj)).get("languages"));
            newOjLanguageMap.put(oj, languageList);
        }
        if(!newOjLanguageMap.isEmpty()) {
            ojLanguageMap = newOjLanguageMap;
        }
    }

    @Override
    public RemoteOjInfo getOjInfo() {
        return VjudgeInfo.INFO;
    }

    @Override
    public boolean isDiverse() {
        return true;
    }

    @Override
    public void getLanguages(String remoteProblemId, Handler<LinkedHashMap<String, String>> handler) {
        if(System.currentTimeMillis() - lastUpdateTime > 1000L * 60 * 60 * 24) {
            lastUpdateTime = System.currentTimeMillis();
            try {
                String json = Jsoup.connect("https://vjudge.net/util/remoteOJs").ignoreContentType(true).timeout(1000 * 10).execute().body();
                log.info("update OjLanguageMap from /vjudge.net, get " + json);
                updateOjLanguageMap(json);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        
        String oj = remoteProblemId.substring(0, remoteProblemId.indexOf("-")).trim();
        handler.handle(ojLanguageMap.get(oj));
    }

    @Override
    public LinkedHashMap<String, String> getDefaultLanguages() {
        return null;
    }

    @Override
    public HashMap<String, String> getLanguagesAdapter() {
        // TODO Auto-generated method stub
        return null;
    }

}
