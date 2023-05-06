package net.wuxianje;

public class TextBlock {

  public static void main(final String[] args) {
    System.out.println(oldWayHtmlText());
    System.out.println();
    System.out.println(textBlockHtml());
  }

  public static String oldWayHtmlText() {
    return "<!DOCTYPE html>\n" +
      "<html lang=\"zh\">\n" +
      "  <head>\n" +
      "    <title>标题</title>\n" +
      "  </head>\n" +
      "  <body>\n" +
      "    <p>你好 HTML</p>\n" +
      "  </body>\n" +
      "</html>";
  }

  public static String textBlockHtml() {
    return """
      <!DOCTYPE html>
      <html lang="zh">
        <head>
          <title>标题</title>
        </head>
        <body>
          <p>你好 HTML</p>
        </body>
      </html>""";
  }
}
