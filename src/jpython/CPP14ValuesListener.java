package jpython;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class CPP14ValuesListener extends CPP14BaseListener {
    private TokenStreamRewriter rewriter;
    public boolean flag;
    private String dir;
    private String file_name;

    private int getParentType(ParserRuleContext ctx) {
        ParserRuleContext parent = ctx.getParent();
        while (!parent.isEmpty()) {
            int index = parent.getRuleIndex();
            switch (index) {
                case CPP14Parser.RULE_functiondefinition: {
                    return 138;
                }
                case CPP14Parser.RULE_initdeclarator: {
                    return 118;
                }
                case CPP14Parser.RULE_parameterdeclarationlist: {
                    return 136;
                }
                case CPP14Parser.RULE_postfixexpression: {
                    if (parent.getParent().getRuleIndex() == CPP14Parser.RULE_postfixexpression && ((CPP14Parser.PostfixexpressionContext) parent.getParent()).LeftParen() != null) {
                        //System.out.println(((CPP14Parser.PostfixexpressionContext) parent.getParent()).LeftParen().getText());
                        return 200; //函数调用
                    } else {
                        return 15; //变量引用
                    }
                }
                default:
                    parent = parent.getParent();
            }
        }
        return -1;
    }

    public CPP14ValuesListener(TokenStream tokens, String dir, String file_name) {
        rewriter = new TokenStreamRewriter(tokens);
        flag = false;
        this.dir = dir;
        this.file_name = file_name;
    }

    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {

    }

    @Override
    public void exitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        //过滤掉语法错误的文件
        if (!flag) {
            String str = rewriter.getText(ctx.getSourceInterval());
            //替换多个空格
            str = str.replaceAll(" +", " ");

            //函数拆分
            try {
                String path = dir + "\\des_code";
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                FileWriter writer = new FileWriter(path + "\\" + file_name);
                writer.write(str);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            rewriter.deleteProgram();
        }
    }

    @Override
    public void enterPrimaryexpression(CPP14Parser.PrimaryexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }


    @Override
    public void enterIdexpression(CPP14Parser.IdexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
        switch (getParentType(ctx)) {
            case CPP14Parser.RULE_functiondefinition: {
                rewriter.replace(ctx.start, Tools.FUNCTION_NAME); //函数名
                break;
            }
            case CPP14Parser.RULE_initdeclarator: {
                rewriter.replace(ctx.start, Tools.VARIABLE_NAMW); //变量名
                break;
            }
            case CPP14Parser.RULE_parameterdeclarationlist: {
                rewriter.replace(ctx.start, Tools.FORMAL_PARAMETER); //函数形参
                break;
            }
            case 200: {  //函数调用
                rewriter.replace(ctx.start, Tools.FUNCTION_CALL);
                break;
            }
            case CPP14Parser.RULE_postfixexpression: { //变量引用
                rewriter.replace(ctx.start, Tools.VARIABLE_CALL);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void enterQualifiedid(CPP14Parser.QualifiedidContext ctx) {
        //作用域运算符调用 std::endl
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNestednamespecifier(CPP14Parser.NestednamespecifierContext ctx) {
        //作用域运算符
        //System.out.println("AAAAAAAAAA = " + ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }


    @Override
    public void enterLambdaexpression(CPP14Parser.LambdaexpressionContext ctx) {
        //Lambda 表达式
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLambdaintroducer(CPP14Parser.LambdaintroducerContext ctx) {
        //Lambda b表达式 : [n,&m]
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLambdacapture(CPP14Parser.LambdacaptureContext ctx) {
        //Lambda b表达式[n,&m]: n,&m
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCapturedefault(CPP14Parser.CapturedefaultContext ctx) {
        // System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCapturelist(CPP14Parser.CapturelistContext ctx) {
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCapture(CPP14Parser.CaptureContext ctx) {
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterSimplecapture(CPP14Parser.SimplecaptureContext ctx) {
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInitcapture(CPP14Parser.InitcaptureContext ctx) {
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLambdadeclarator(CPP14Parser.LambdadeclaratorContext ctx) {
        // System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        //System.out.println(ctx.getText());
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }


    @Override
    public void exitPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {

        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypeidofexpr(CPP14Parser.TypeidofexprContext ctx) {

        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypeidofexpr(CPP14Parser.TypeidofexprContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypeidofthetypeid(CPP14Parser.TypeidofthetypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypeidofthetypeid(CPP14Parser.TypeidofthetypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExpressionlist(CPP14Parser.ExpressionlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExpressionlist(CPP14Parser.ExpressionlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPseudodestructorname(CPP14Parser.PseudodestructornameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPseudodestructorname(CPP14Parser.PseudodestructornameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUnaryexpression(CPP14Parser.UnaryexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitUnaryexpression(CPP14Parser.UnaryexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUnaryoperator(CPP14Parser.UnaryoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitUnaryoperator(CPP14Parser.UnaryoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNewexpression(CPP14Parser.NewexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNewexpression(CPP14Parser.NewexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNewplacement(CPP14Parser.NewplacementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNewplacement(CPP14Parser.NewplacementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNewtypeid(CPP14Parser.NewtypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNewtypeid(CPP14Parser.NewtypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNewdeclarator(CPP14Parser.NewdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNewdeclarator(CPP14Parser.NewdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNoptrnewdeclarator(CPP14Parser.NoptrnewdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNoptrnewdeclarator(CPP14Parser.NoptrnewdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNewinitializer(CPP14Parser.NewinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNewinitializer(CPP14Parser.NewinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeleteexpression(CPP14Parser.DeleteexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDeleteexpression(CPP14Parser.DeleteexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNoexceptexpression(CPP14Parser.NoexceptexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNoexceptexpression(CPP14Parser.NoexceptexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCastexpression(CPP14Parser.CastexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitCastexpression(CPP14Parser.CastexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPmexpression(CPP14Parser.PmexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPmexpression(CPP14Parser.PmexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMultiplicativeexpression(CPP14Parser.MultiplicativeexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAdditiveexpression(CPP14Parser.AdditiveexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAdditiveexpression(CPP14Parser.AdditiveexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterShiftexpression(CPP14Parser.ShiftexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitShiftexpression(CPP14Parser.ShiftexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterShiftoperator(CPP14Parser.ShiftoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitShiftoperator(CPP14Parser.ShiftoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterRelationalexpression(CPP14Parser.RelationalexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitRelationalexpression(CPP14Parser.RelationalexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEqualityexpression(CPP14Parser.EqualityexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEqualityexpression(CPP14Parser.EqualityexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAndexpression(CPP14Parser.AndexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAndexpression(CPP14Parser.AndexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExclusiveorexpression(CPP14Parser.ExclusiveorexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExclusiveorexpression(CPP14Parser.ExclusiveorexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInclusiveorexpression(CPP14Parser.InclusiveorexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitInclusiveorexpression(CPP14Parser.InclusiveorexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLogicalandexpression(CPP14Parser.LogicalandexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitLogicalandexpression(CPP14Parser.LogicalandexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLogicalorexpression(CPP14Parser.LogicalorexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitLogicalorexpression(CPP14Parser.LogicalorexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterConditionalexpression(CPP14Parser.ConditionalexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitConditionalexpression(CPP14Parser.ConditionalexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAssignmentoperator(CPP14Parser.AssignmentoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAssignmentoperator(CPP14Parser.AssignmentoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExpression(CPP14Parser.ExpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExpression(CPP14Parser.ExpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterConstantexpression(CPP14Parser.ConstantexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitConstantexpression(CPP14Parser.ConstantexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterStatement(CPP14Parser.StatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitStatement(CPP14Parser.StatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLabeledstatement(CPP14Parser.LabeledstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitLabeledstatement(CPP14Parser.LabeledstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExpressionstatement(CPP14Parser.ExpressionstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExpressionstatement(CPP14Parser.ExpressionstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCompoundstatement(CPP14Parser.CompoundstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitCompoundstatement(CPP14Parser.CompoundstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterStatementseq(CPP14Parser.StatementseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitStatementseq(CPP14Parser.StatementseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterSelectionstatement(CPP14Parser.SelectionstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitSelectionstatement(CPP14Parser.SelectionstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCondition(CPP14Parser.ConditionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitCondition(CPP14Parser.ConditionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterIterationstatement(CPP14Parser.IterationstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");

        //while( 两者之间没有空格
        if (ctx.While() != null) {
            rewriter.insertAfter(ctx.start, " ");
        }

        //for( 两者之间没有空格
        if (ctx.For() != null) {
            rewriter.insertAfter(ctx.start, " ");
        }

        //do{}while(true); 最后的 ); 之间没有空格
        if (ctx.Do() != null) {
            rewriter.replace(ctx.stop, " ; ");
        }
    }

    @Override
    public void exitIterationstatement(CPP14Parser.IterationstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterForinitstatement(CPP14Parser.ForinitstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitForinitstatement(CPP14Parser.ForinitstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterForrangedeclaration(CPP14Parser.ForrangedeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitForrangedeclaration(CPP14Parser.ForrangedeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterForrangeinitializer(CPP14Parser.ForrangeinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitForrangeinitializer(CPP14Parser.ForrangeinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterJumpstatement(CPP14Parser.JumpstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitJumpstatement(CPP14Parser.JumpstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeclarationstatement(CPP14Parser.DeclarationstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDeclarationstatement(CPP14Parser.DeclarationstatementContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeclarationseq(CPP14Parser.DeclarationseqContext ctx) {
        if (ctx != null) {
            rewriter.insertBefore(ctx.start, " ");
            if (ctx.stop != null) {
                rewriter.insertAfter(ctx.stop, " ");
            } else flag = true;
        } else {
            flag = true;
        }
    }

    @Override
    public void exitDeclarationseq(CPP14Parser.DeclarationseqContext ctx) {
        if (ctx != null) {
            rewriter.insertBefore(ctx.start, " ");
            if (ctx.stop != null) {
                rewriter.insertAfter(ctx.stop, " ");
            } else flag = true;
        } else {
            flag = true;
        }
    }

    @Override
    public void enterDeclaration(CPP14Parser.DeclarationContext ctx) {
        if (ctx != null) {
            rewriter.insertBefore(ctx.start, " ");
            if (ctx.stop != null) {
                rewriter.insertAfter(ctx.stop, " ");
            } else flag = true;
        } else {
            flag = true;
        }
    }

    @Override
    public void exitDeclaration(CPP14Parser.DeclarationContext ctx) {
        if (ctx != null) {
            rewriter.insertBefore(ctx.start, " ");
            if (ctx.stop != null) {
                rewriter.insertAfter(ctx.stop, " ");
            } else flag = true;
        } else {
            flag = true;
        }
    }

    @Override
    public void enterBlockdeclaration(CPP14Parser.BlockdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBlockdeclaration(CPP14Parser.BlockdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAliasdeclaration(CPP14Parser.AliasdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAliasdeclaration(CPP14Parser.AliasdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitSimpledeclaration(CPP14Parser.SimpledeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterStatic_assertdeclaration(CPP14Parser.Static_assertdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitStatic_assertdeclaration(CPP14Parser.Static_assertdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEmptydeclaration(CPP14Parser.EmptydeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEmptydeclaration(CPP14Parser.EmptydeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributedeclaration(CPP14Parser.AttributedeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributedeclaration(CPP14Parser.AttributedeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeclspecifier(CPP14Parser.DeclspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDeclspecifier(CPP14Parser.DeclspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeclspecifierseq(CPP14Parser.DeclspecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDeclspecifierseq(CPP14Parser.DeclspecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterStorageclassspecifier(CPP14Parser.StorageclassspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitStorageclassspecifier(CPP14Parser.StorageclassspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypedefname(CPP14Parser.TypedefnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypedefname(CPP14Parser.TypedefnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypespecifier(CPP14Parser.TypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypespecifier(CPP14Parser.TypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTrailingtypespecifier(CPP14Parser.TrailingtypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTrailingtypespecifier(CPP14Parser.TrailingtypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypespecifierseq(CPP14Parser.TypespecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypespecifierseq(CPP14Parser.TypespecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTrailingtypespecifierseq(CPP14Parser.TrailingtypespecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTrailingtypespecifierseq(CPP14Parser.TrailingtypespecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitSimpletypespecifier(CPP14Parser.SimpletypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterThetypename(CPP14Parser.ThetypenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitThetypename(CPP14Parser.ThetypenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDecltypespecifier(CPP14Parser.DecltypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDecltypespecifier(CPP14Parser.DecltypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterElaboratedtypespecifier(CPP14Parser.ElaboratedtypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitElaboratedtypespecifier(CPP14Parser.ElaboratedtypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumname(CPP14Parser.EnumnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumname(CPP14Parser.EnumnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumspecifier(CPP14Parser.EnumspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumspecifier(CPP14Parser.EnumspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumhead(CPP14Parser.EnumheadContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumhead(CPP14Parser.EnumheadContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterOpaqueenumdeclaration(CPP14Parser.OpaqueenumdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitOpaqueenumdeclaration(CPP14Parser.OpaqueenumdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumkey(CPP14Parser.EnumkeyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumkey(CPP14Parser.EnumkeyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumbase(CPP14Parser.EnumbaseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumbase(CPP14Parser.EnumbaseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumeratorlist(CPP14Parser.EnumeratorlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumeratorlist(CPP14Parser.EnumeratorlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumeratordefinition(CPP14Parser.EnumeratordefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumeratordefinition(CPP14Parser.EnumeratordefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEnumerator(CPP14Parser.EnumeratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEnumerator(CPP14Parser.EnumeratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNamespacename(CPP14Parser.NamespacenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNamespacename(CPP14Parser.NamespacenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterOriginalnamespacename(CPP14Parser.OriginalnamespacenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitOriginalnamespacename(CPP14Parser.OriginalnamespacenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNamespacedefinition(CPP14Parser.NamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNamespacedefinition(CPP14Parser.NamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNamednamespacedefinition(CPP14Parser.NamednamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNamednamespacedefinition(CPP14Parser.NamednamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterOriginalnamespacedefinition(CPP14Parser.OriginalnamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitOriginalnamespacedefinition(CPP14Parser.OriginalnamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExtensionnamespacedefinition(CPP14Parser.ExtensionnamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExtensionnamespacedefinition(CPP14Parser.ExtensionnamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUnnamednamespacedefinition(CPP14Parser.UnnamednamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitUnnamednamespacedefinition(CPP14Parser.UnnamednamespacedefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNamespacebody(CPP14Parser.NamespacebodyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNamespacebody(CPP14Parser.NamespacebodyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNamespacealias(CPP14Parser.NamespacealiasContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNamespacealias(CPP14Parser.NamespacealiasContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNamespacealiasdefinition(CPP14Parser.NamespacealiasdefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNamespacealiasdefinition(CPP14Parser.NamespacealiasdefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterQualifiednamespacespecifier(CPP14Parser.QualifiednamespacespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitQualifiednamespacespecifier(CPP14Parser.QualifiednamespacespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUsingdeclaration(CPP14Parser.UsingdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitUsingdeclaration(CPP14Parser.UsingdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUsingdirective(CPP14Parser.UsingdirectiveContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitUsingdirective(CPP14Parser.UsingdirectiveContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAsmdefinition(CPP14Parser.AsmdefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAsmdefinition(CPP14Parser.AsmdefinitionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLinkagespecification(CPP14Parser.LinkagespecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitLinkagespecification(CPP14Parser.LinkagespecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributespecifierseq(CPP14Parser.AttributespecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributespecifierseq(CPP14Parser.AttributespecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributespecifier(CPP14Parser.AttributespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributespecifier(CPP14Parser.AttributespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAlignmentspecifier(CPP14Parser.AlignmentspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAlignmentspecifier(CPP14Parser.AlignmentspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributelist(CPP14Parser.AttributelistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributelist(CPP14Parser.AttributelistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttribute(CPP14Parser.AttributeContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttribute(CPP14Parser.AttributeContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributetoken(CPP14Parser.AttributetokenContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributetoken(CPP14Parser.AttributetokenContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributescopedtoken(CPP14Parser.AttributescopedtokenContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributescopedtoken(CPP14Parser.AttributescopedtokenContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributenamespace(CPP14Parser.AttributenamespaceContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributenamespace(CPP14Parser.AttributenamespaceContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAttributeargumentclause(CPP14Parser.AttributeargumentclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAttributeargumentclause(CPP14Parser.AttributeargumentclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBalancedtokenseq(CPP14Parser.BalancedtokenseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBalancedtokenseq(CPP14Parser.BalancedtokenseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBalancedtoken(CPP14Parser.BalancedtokenContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBalancedtoken(CPP14Parser.BalancedtokenContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInitdeclaratorlist(CPP14Parser.InitdeclaratorlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitInitdeclaratorlist(CPP14Parser.InitdeclaratorlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInitdeclarator(CPP14Parser.InitdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitInitdeclarator(CPP14Parser.InitdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeclarator(CPP14Parser.DeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDeclarator(CPP14Parser.DeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPtrdeclarator(CPP14Parser.PtrdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPtrdeclarator(CPP14Parser.PtrdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNoptrdeclarator(CPP14Parser.NoptrdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");

    }

    @Override
    public void exitNoptrdeclarator(CPP14Parser.NoptrdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");

    }

    @Override
    public void enterParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
        ctx.LeftParen();

    }

    @Override
    public void exitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTrailingreturntype(CPP14Parser.TrailingreturntypeContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTrailingreturntype(CPP14Parser.TrailingreturntypeContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPtroperator(CPP14Parser.PtroperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPtroperator(CPP14Parser.PtroperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCvqualifierseq(CPP14Parser.CvqualifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitCvqualifierseq(CPP14Parser.CvqualifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCvqualifier(CPP14Parser.CvqualifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitCvqualifier(CPP14Parser.CvqualifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterRefqualifier(CPP14Parser.RefqualifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitRefqualifier(CPP14Parser.RefqualifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDeclaratorid(CPP14Parser.DeclaratoridContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDeclaratorid(CPP14Parser.DeclaratoridContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterThetypeid(CPP14Parser.ThetypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitThetypeid(CPP14Parser.ThetypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAbstractdeclarator(CPP14Parser.AbstractdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAbstractdeclarator(CPP14Parser.AbstractdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPtrabstractdeclarator(CPP14Parser.PtrabstractdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPtrabstractdeclarator(CPP14Parser.PtrabstractdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNoptrabstractdeclarator(CPP14Parser.NoptrabstractdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNoptrabstractdeclarator(CPP14Parser.NoptrabstractdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAbstractpackdeclarator(CPP14Parser.AbstractpackdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAbstractpackdeclarator(CPP14Parser.AbstractpackdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNoptrabstractpackdeclarator(CPP14Parser.NoptrabstractpackdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNoptrabstractpackdeclarator(CPP14Parser.NoptrabstractpackdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterParameterdeclarationclause(CPP14Parser.ParameterdeclarationclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitParameterdeclarationclause(CPP14Parser.ParameterdeclarationclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterParameterdeclarationlist(CPP14Parser.ParameterdeclarationlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitParameterdeclarationlist(CPP14Parser.ParameterdeclarationlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
        //System.out.println("exitParameterdeclarationlist" + ctx.getRuleIndex());
    }

    @Override
    public void enterParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
        if (ctx.getText().equals("{}")) {
            rewriter.insertAfter(ctx.start, " ");
        }
    }

    @Override
    public void exitFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInitializer(CPP14Parser.InitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitInitializer(CPP14Parser.InitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBraceorequalinitializer(CPP14Parser.BraceorequalinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBraceorequalinitializer(CPP14Parser.BraceorequalinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInitializerclause(CPP14Parser.InitializerclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitInitializerclause(CPP14Parser.InitializerclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterInitializerlist(CPP14Parser.InitializerlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitInitializerlist(CPP14Parser.InitializerlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBracedinitlist(CPP14Parser.BracedinitlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBracedinitlist(CPP14Parser.BracedinitlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClassname(CPP14Parser.ClassnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
        rewriter.replace(ctx.start, Tools.CLASS_NAME);
    }

    @Override
    public void exitClassname(CPP14Parser.ClassnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClasshead(CPP14Parser.ClassheadContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitClasshead(CPP14Parser.ClassheadContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClassheadname(CPP14Parser.ClassheadnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitClassheadname(CPP14Parser.ClassheadnameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClassvirtspecifier(CPP14Parser.ClassvirtspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitClassvirtspecifier(CPP14Parser.ClassvirtspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClasskey(CPP14Parser.ClasskeyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitClasskey(CPP14Parser.ClasskeyContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMemberdeclaratorlist(CPP14Parser.MemberdeclaratorlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMemberdeclaratorlist(CPP14Parser.MemberdeclaratorlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMemberdeclarator(CPP14Parser.MemberdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMemberdeclarator(CPP14Parser.MemberdeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterVirtspecifierseq(CPP14Parser.VirtspecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitVirtspecifierseq(CPP14Parser.VirtspecifierseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterVirtspecifier(CPP14Parser.VirtspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitVirtspecifier(CPP14Parser.VirtspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPurespecifier(CPP14Parser.PurespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPurespecifier(CPP14Parser.PurespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBaseclause(CPP14Parser.BaseclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBaseclause(CPP14Parser.BaseclauseContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBasespecifierlist(CPP14Parser.BasespecifierlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBasespecifierlist(CPP14Parser.BasespecifierlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBasespecifier(CPP14Parser.BasespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBasespecifier(CPP14Parser.BasespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterClassordecltype(CPP14Parser.ClassordecltypeContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitClassordecltype(CPP14Parser.ClassordecltypeContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBasetypespecifier(CPP14Parser.BasetypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBasetypespecifier(CPP14Parser.BasetypespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterAccessspecifier(CPP14Parser.AccessspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitAccessspecifier(CPP14Parser.AccessspecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterConversionfunctionid(CPP14Parser.ConversionfunctionidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitConversionfunctionid(CPP14Parser.ConversionfunctionidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterConversiontypeid(CPP14Parser.ConversiontypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitConversiontypeid(CPP14Parser.ConversiontypeidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterConversiondeclarator(CPP14Parser.ConversiondeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitConversiondeclarator(CPP14Parser.ConversiondeclaratorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterCtorinitializer(CPP14Parser.CtorinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitCtorinitializer(CPP14Parser.CtorinitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMeminitializerlist(CPP14Parser.MeminitializerlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMeminitializerlist(CPP14Parser.MeminitializerlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMeminitializer(CPP14Parser.MeminitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMeminitializer(CPP14Parser.MeminitializerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterMeminitializerid(CPP14Parser.MeminitializeridContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitMeminitializerid(CPP14Parser.MeminitializeridContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterOperatorfunctionid(CPP14Parser.OperatorfunctionidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitOperatorfunctionid(CPP14Parser.OperatorfunctionidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLiteraloperatorid(CPP14Parser.LiteraloperatoridContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitLiteraloperatorid(CPP14Parser.LiteraloperatoridContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTemplatedeclaration(CPP14Parser.TemplatedeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplatedeclaration(CPP14Parser.TemplatedeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTemplateparameterlist(CPP14Parser.TemplateparameterlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplateparameterlist(CPP14Parser.TemplateparameterlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }


    @Override
    public void enterTemplateparameter(CPP14Parser.TemplateparameterContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplateparameter(CPP14Parser.TemplateparameterContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypeparameter(CPP14Parser.TypeparameterContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypeparameter(CPP14Parser.TypeparameterContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterSimpletemplateid(CPP14Parser.SimpletemplateidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitSimpletemplateid(CPP14Parser.SimpletemplateidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTemplateid(CPP14Parser.TemplateidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplateid(CPP14Parser.TemplateidContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTemplatename(CPP14Parser.TemplatenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplatename(CPP14Parser.TemplatenameContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTemplateargumentlist(CPP14Parser.TemplateargumentlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplateargumentlist(CPP14Parser.TemplateargumentlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTemplateargument(CPP14Parser.TemplateargumentContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTemplateargument(CPP14Parser.TemplateargumentContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypenamespecifier(CPP14Parser.TypenamespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypenamespecifier(CPP14Parser.TypenamespecifierContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExplicitinstantiation(CPP14Parser.ExplicitinstantiationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExplicitinstantiation(CPP14Parser.ExplicitinstantiationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExplicitspecialization(CPP14Parser.ExplicitspecializationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExplicitspecialization(CPP14Parser.ExplicitspecializationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTryblock(CPP14Parser.TryblockContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTryblock(CPP14Parser.TryblockContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterFunctiontryblock(CPP14Parser.FunctiontryblockContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitFunctiontryblock(CPP14Parser.FunctiontryblockContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterHandlerseq(CPP14Parser.HandlerseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitHandlerseq(CPP14Parser.HandlerseqContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterHandler(CPP14Parser.HandlerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitHandler(CPP14Parser.HandlerContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExceptiondeclaration(CPP14Parser.ExceptiondeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExceptiondeclaration(CPP14Parser.ExceptiondeclarationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterThrowexpression(CPP14Parser.ThrowexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitThrowexpression(CPP14Parser.ThrowexpressionContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterExceptionspecification(CPP14Parser.ExceptionspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitExceptionspecification(CPP14Parser.ExceptionspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterDynamicexceptionspecification(CPP14Parser.DynamicexceptionspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitDynamicexceptionspecification(CPP14Parser.DynamicexceptionspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTypeidlist(CPP14Parser.TypeidlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTypeidlist(CPP14Parser.TypeidlistContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterNoexceptspecification(CPP14Parser.NoexceptspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitNoexceptspecification(CPP14Parser.NoexceptspecificationContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterTheoperator(CPP14Parser.TheoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitTheoperator(CPP14Parser.TheoperatorContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterLiteral(CPP14Parser.LiteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
        String str = ctx.getText();
        String val = "\"thisisstring";
        Vector<String> vec = new Vector<>();
        if (!Tools.isContainFORMAT_OUTPUT(str, vec)) {
            for (String tmp : vec
            ) {
                val += tmp;
            }
            val += "\"";
            rewriter.replace(ctx.start, val);
        } else {
            if (str.indexOf("'") == 0) {
                rewriter.replace(ctx.start, Tools.CHAR);
            }
            if (str.indexOf("\"") == 0) {
                rewriter.replace(ctx.start, Tools.STRING);
            }
        }
    }

    @Override
    public void exitLiteral(CPP14Parser.LiteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterBooleanliteral(CPP14Parser.BooleanliteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitBooleanliteral(CPP14Parser.BooleanliteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterPointerliteral(CPP14Parser.PointerliteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitPointerliteral(CPP14Parser.PointerliteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterUserdefinedliteral(CPP14Parser.UserdefinedliteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitUserdefinedliteral(CPP14Parser.UserdefinedliteralContext ctx) {
        rewriter.insertBefore(ctx.start, " ");
        rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        //rewriter.insertBefore(ctx.start, " ");
        //rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        // rewriter.insertBefore(ctx.start, " ");
        //rewriter.insertAfter(ctx.stop, " ");
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        //System.out.println(node.getText());
        rewriter.insertBefore(node.getSymbol().getStartIndex(), " ");
        rewriter.insertAfter(node.getSymbol().getStopIndex(), " ");
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        if (node != null) {
            flag = true;
        }
    }
}
