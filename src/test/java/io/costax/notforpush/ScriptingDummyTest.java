package io.costax.notforpush;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class ScriptingDummyTest {

    @Test
    public void name() {
        ScriptEngineManager manager = new ScriptEngineManager();

        // Get the list of all available engines
        List<ScriptEngineFactory> list = manager.getEngineFactories();

        // Print the details of each engine
        for (ScriptEngineFactory f : list) {
            System.out.println("Engine Name:" + f.getEngineName());
            System.out.println("Engine Version:" + f.getEngineVersion());
            System.out.println("Language Name:" + f.getLanguageName());
            System.out.println("Language Version:" + f.getLanguageVersion());
            System.out.println("Engine Short Names:" + f.getNames());
            System.out.println("Mime Types:" + f.getMimeTypes());
            System.out.println("===");
        }

    }

    @Test
    public void execute() {
        // Get the script engine manager
        ScriptEngineManager manager = new ScriptEngineManager();

        // Try executing scripts in Nashorn, Groovy, Jython, and JRuby
        execute(manager, "JavaScript", "print('Hello JavaScript')");
        execute(manager, "Groovy", "println('Hello Groovy')");
        execute(manager, "jython", "print 'Hello Jython'");
        execute(manager, "jruby", "puts('Hello JRuby')");
    }

    public static void execute(ScriptEngineManager manager, String engineName, String script) {

        ScriptEngine engine = manager.getEngineByName(engineName);
        if (engine == null) {
            System.out.println(engineName + " is not available.");
            return;
        }

        try {
            engine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}

