/**
 * The MIT License
 *
 * Copyright 2017 Maksim Kostromin
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * https://tldrlegal.com/license/mit-license
 */
package io.github.daggerok.utils

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.gradle.api.GradleException
import org.gradle.api.Project

import java.text.DateFormat

@Slf4j
@CompileStatic
class ExecutionUtils {

  static final String usage(final String taskName) {
    """\
       |Please, make sure you properly configured $taskName task.
       |Minimal required configuration is existed SoapUI test project file.
       |Default path is: "\${projectDir}/soapui-test-project.xml"
       |
       |$taskName {
       |  projectFile = 'soapui-test-project.xml'
       |}""".stripMargin()
  }

  static void validate(final File file, final String taskName, final boolean failOnError) {

    if (file.exists() && file.canRead()) return

    def message = "file $file doesn't exists or not accessible.\n${usage(taskName)}"
    log.error message
    if (failOnError) throw new GradleException(message)
  }

  static String today() {
    DateFormat.getDateTimeInstance().format new Date()
  }

  /**
   * Try execute clojure. If error occurs then just log it and continue execution...
   * <br/>
   * @param closure closure to be executed.
   */
  static void tryWithLog(final Closure closure) {

    try {

      closure()

    } catch (final Exception e) {

      log.error "continue execution with error: {}", e?.localizedMessage, e
    }
  }

  /**
   * Try execute clojure. If error occurs use message in addition.
   * <br/>
   * @param message message to be added.
   * @param closure closure to be executed.
   */
  @SuppressWarnings("unused")
  static void tryWithLogMessage(final String message, final Closure closure) {

    try {

      closure()

    } catch (final Exception e) {

      log.error "continue execution, but: {}\n{}", message, e?.localizedMessage, e
    }
  }

  /**
   * Try execute clojure. If error occurs use message in addition to exception.
   * <br/>
   * @param message message to be added.
   * @param closure closure to be executed.
   */
  static void tryWithMessage(final String message, final Closure closure) {

    try {

      closure()

    } catch (final Exception e) {

      def msg = "${e?.localizedMessage}\n$message"
      log.error "execution fails: {}", msg, e
      throw new GradleException(msg)
    }
  }

  /**
   * try execute closure content. If error will occurs, re-throw an error only if failOnError is true.
   *
   * @param failOnError throw an error if execution will fails.
   * @param closure closure to be executed.
   */
  static void tryOrThrowIf(final boolean failOnError, final Closure closure) {

    try {

      closure()

    } catch (final Exception e) {

      def message = e?.localizedMessage
      log.error "execution error: {}", message, e
      if (failOnError) throw new GradleException(message)
    }
  }

  /**
   * SoapUI runner looking for ext folder.
   * All inside could be used as external scripts or libraries.
   * <br/>
   * I will create that folder if it's not exists.
   * Otherwise if it's exists and it's not a directory it might cause some unexpected issues...
   *
   * @param project applied to
   */
  static void createExtFolder(final Project project) {

    final String extDirName = 'ext'
    final File extDir = project.file(extDirName)

    if (extDir.exists()) {
      if (extDir.isDirectory()) return
      log.info "$extDirName is reserved name for SoapUI libraries folder."
      return
    }

    tryWithLog {
      project.mkdir(extDirName)
    }
  }

  private ExecutionUtils() {}
}
