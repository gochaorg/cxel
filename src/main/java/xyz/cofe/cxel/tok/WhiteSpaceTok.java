package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;

import java.util.List;

/**
 * Пробельные символы
 */
public class WhiteSpaceTok extends CToken {
    public WhiteSpaceTok( List<CToken> tokens ) { super(tokens); }

    public WhiteSpaceTok( WhiteSpaceTok sample ){
        super(sample);
    }

    public WhiteSpaceTok clone(){ return new WhiteSpaceTok(this); }
}
