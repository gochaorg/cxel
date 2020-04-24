package xyz.cofe.cxel.js;

/**
 * Неопределенный тип в js.
 * <a href="https://developer.mozilla.org/ru/docs/Web/JavaScript/Reference/Global_Objects/undefined">см Mozilla.</a>
 */
public final class Undef {
    /**
     * Закрываем к чертям всякое наследование
     */
    private Undef(){}

    /**
     * Может существовать только один undef, Горец блин
     */
    public static final Undef instance = new Undef();

    @Override
    public String toString(){
        return "undefined";
    }
}
