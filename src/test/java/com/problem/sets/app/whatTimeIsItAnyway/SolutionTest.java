package com.problem.sets.app.whatTimeIsItAnyway;

import com.google.common.collect.Streams;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SolutionTest {
  private static final String INPUT_FILE
      = "src/test/resources/input.txt";
  private static final String OUTPUT_FILE
      = "src/test/resources/output.txt";
  private static final long TIME_OUT = 10_000_000L;

  private Solution solution;

  @BeforeEach
  public void setUp() {
    solution = new Solution();
  }

  @Test
  @Timeout(unit = TimeUnit.NANOSECONDS, value = TIME_OUT)
  public void testSolve_returnTime() {
    String res = solution
        .solve(List.of("1:05", "8:35"), List.of("-3:15", "+1:15"));
    assertNotNull(res);
    assertEquals("11:50", res);
  }

  @Test
  @Timeout(unit = TimeUnit.NANOSECONDS, value = TIME_OUT)
  public void testSolve_returnMoreThanOneTime() {
    String res = solution
        .solve(List.of("3:00", "9:00"), List.of("+3:00", "-3:00"));
    assertNotNull(res);
    assertEquals("2", res);
  }

  @Test
  @Timeout(unit = TimeUnit.NANOSECONDS, value = TIME_OUT)
  public void testSolve_returnNone() {
    String res = solution
        .solve(List.of("3:00", "9:00"), List.of("+6:00", "-6:00"));
    assertNotNull(res);
    assertEquals("none", res);
  }

  @DisplayName("Test from external resources")
  @ParameterizedTest(name = "[{index}] clocks: {0} labels: {1} expected: {2}")
  @MethodSource("provideTestsFromSources")
  @Timeout(unit = TimeUnit.NANOSECONDS, value = TIME_OUT)
  public void testSolve_withExternalSource(
      List<String> clocks, List<String> labels, String expected) {
    String res = solution.solve(clocks, labels);
    assertNotNull(res);
    assertEquals(expected, res);
  }

  private static Stream<Arguments> provideTestsFromSources()
      throws IOException {
    List<Arguments> inputs = readInputFile();
    List<Arguments> outputs = readOutputFile();
    return Streams.zip(inputs.stream(), outputs.stream(),
        SolutionTest::mergeArguments);
  }

  private static Arguments mergeArguments(
      Arguments arg1, Arguments arg2) {
    return Arguments.of(arg1.get()[0], arg1.get()[1], arg2.get()[0]);
  }

  private static List<Arguments> readInputFile()
      throws IOException {
    InputStream input
        = new FileInputStream(Paths.get(INPUT_FILE).toFile());
    BufferedReader br
        = new BufferedReader(new InputStreamReader(input));

    int testCases = Integer.parseInt(br.readLine());

    List<Arguments> result = new ArrayList<>();
    for (int i = 0; i < testCases; i++) {
      int n = Integer.parseInt(br.readLine().split(" ")[1]);

      List<String> clocks = new ArrayList<>();
      for (int j = 0; j < n; j++) {
        clocks.add(br.readLine());
      }

      List<String> labels = new ArrayList<>();
      for (int j = 0; j < n; j++) {
        labels.add(br.readLine());
      }

      result.add(Arguments.of(clocks, labels));
    }

    input.close();
    br.close();

    return result;
  }

  private static List<Arguments> readOutputFile()
      throws IOException {
    InputStream input
        = new FileInputStream(Paths.get(OUTPUT_FILE).toFile());
    BufferedReader br
        = new BufferedReader(new InputStreamReader(input));

    List<Arguments> result = new ArrayList<>();
    String line;
    while ((line = br.readLine()) != null) {
      result.add(Arguments.of(line.split(" ")[1]));
    }

    input.close();
    br.close();

    return result;
  }
}
