import java.util.regex.Pattern

class Test {
  static void main(String[] args) {
    String path = '__package__-model'
    println path.replaceAll('.*__(\\w+)__.*', '\\$$1')
  }
}
