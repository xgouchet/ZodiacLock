package fr.xgouchet.zodiaclock.engine.environment;

import android.support.annotation.NonNull;

import fr.xgouchet.zodiaclock.engine.GLException;
import fr.xgouchet.zodiaclock.engine.RenderContext;
import fr.xgouchet.zodiaclock.engine.Transform;


/**
 * @author Xavier Gouchet
 */
public class Light extends Transform {


    @Override
    public void onRender(@NonNull RenderContext renderContext) throws GLException {
        copy(renderContext.vecLightPos, 3, 12);
    }
}
