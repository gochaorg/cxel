package xyz.cofe.cxel.eval.score;

import xyz.cofe.cxel.eval.CallScoring;
import xyz.cofe.cxel.eval.PreparedCall;

public class DefaultScrolling implements CallScoring<PreparedCall> {
    @Override
    public int calculate( PreparedCall preparedCall ){
        if( preparedCall==null )throw new IllegalArgumentException( "preparedCall==null" );
        if( preparedCall instanceof ParameterCount ){
            int score = 0;
            int pcount = ((ParameterCount) preparedCall).parameterCount();

            if( preparedCall instanceof ArgsCasing ){
                int argsCasing = ((ArgsCasing) preparedCall).argsCasing();
                score += argsCasing*Math.pow(pcount,5);
            }

            if( preparedCall instanceof InvariantArgs ){
                int invCalls = ((InvariantArgs) preparedCall).invariantArgs();
                score += invCalls*Math.pow(pcount,0);
            }

            if( preparedCall instanceof PrimitiveCastArgs ){
                int primCastCalls = ((PrimitiveCastArgs) preparedCall).primitiveCastArgs();
                score += primCastCalls*Math.pow(pcount,1);
            }

            if( preparedCall instanceof CastLooseDataArgs ){
                int loseDataCalls = ((CastLooseDataArgs) preparedCall).castLooseDataArgs();
                score += loseDataCalls*Math.pow(pcount,4);
            }

            if( preparedCall instanceof CovariantArgs ){
                int coCalls = ((CovariantArgs) preparedCall).covariantArgs();
                score += coCalls*Math.pow(pcount,2);
            }

            if( preparedCall instanceof ImplicitArgs ){
                int implCalls = ((ImplicitArgs) preparedCall).implicitArgs();
                score += implCalls*Math.pow(pcount,3);
            }

            return score;
        }
        return 0;
    }
}
