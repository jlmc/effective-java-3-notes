package io.costax.overviewmovingtojdk12.processes;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProcessTest {

    @Test
    @Disabled
    void before_jdk_9() throws IOException {

        final Process jps = new ProcessBuilder().command("jps").start();

        //final ProcessHandle.Info info = jps.info();
        // this is cool but is necessary to using native tools to get process information
    }

    @Test
    void with_jdk_9() {
        final ProcessHandle.Info info = ProcessHandle.current().info();

        final Optional<String> s = info.commandLine();

        System.out.println(s);
    }

    @Test
    void get_all_process() {
        final List<Instant> collect = ProcessHandle.allProcesses()
                .map(p -> p.info().startInstant())
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        final List<String> commands = ProcessHandle.allProcesses()
                .map(p -> p.info().command())
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        commands.forEach(System.out::println);
    }

    @Test
    void start_and_wait_for_a_process() throws IOException, InterruptedException, ExecutionException {
        // create a external process

        final CompletableFuture<ProcessHandle> sleep = new ProcessBuilder().command("sleep", "3")
                .start()
                .toHandle()
                .onExit();

        sleep.thenAccept(e -> System.out.println(" ---- " + e)).get(); // the get is just to for the wait of the end of the process

        Thread.sleep(5);
    }

    @Test
    void start_a_pipe_line() throws IOException {
        final ProcessBuilder jps = new ProcessBuilder().command("jps");
        final ProcessBuilder grep = new ProcessBuilder().command("grep", "JShell");

        final List<Process> pipeline = ProcessBuilder.startPipeline(List.of(jps, grep));

        pipeline.get(1).getInputStream().transferTo(System.out);

    }
}
