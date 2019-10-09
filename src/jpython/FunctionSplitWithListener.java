package jpython;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;


public class FunctionSplitWithListener extends CPP14BaseListener {

    private String dir;
    private String file_name;
    private TokenStreamRewriter rewriter;
    public static int count = 0;

    public FunctionSplitWithListener(TokenStream tokens, String dir, String file_name) {
        rewriter = new TokenStreamRewriter(tokens);
        this.dir = dir;
        this.file_name = file_name;
    }

    @Override
    public void exitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        String str = rewriter.getText(ctx.getSourceInterval());
        str = str.replaceAll(" +", " ");
        //函数拆分
        try {
            String path = dir + "\\code";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }

            FileWriter writer = new FileWriter(path + "\\" + file_name + "@" + count + ".cpp");
            writer.write(str);
            writer.close();
            count++;
            rewriter.deleteProgram();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }
}
