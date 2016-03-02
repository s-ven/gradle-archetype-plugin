import java.util.regex.Pattern

class Test {
  static void main(String[] args) {
    String path = '__projectName__-model/src/main/java/__packagePath__/model'
    String newPath = ''
    path.split('/').each {
      println "----- $it"
      if (it.contains('__')) {
        newPath += resolvePath(it)
      } else {
        newPath += it
      }

      newPath += '/'
    }

    println "------------------ $newPath"
  }

  static String resolvePath(String path) {
    path.replaceAll('(.*)__(\\w+)__(.*)', '$1\\$\\{$2\\}$3')
  }
}
