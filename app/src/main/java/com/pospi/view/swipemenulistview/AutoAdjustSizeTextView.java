package com.pospi.view.swipemenulistview;

import android.widget.TextView;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

public class AutoAdjustSizeTextView extends TextView {
    private static float DEFAULT_MIN_TEXT_SIZE = 10;
    private static float DEFAULT_MAX_TEXT_SIZE = 13;

    // Attributes
    private Paint testPaint;
    private float minTextSize, maxTextSize;

    public AutoAdjustSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialise();
    }

    private void initialise() {
        testPaint = new Paint();
        testPaint.set(this.getPaint());


        maxTextSize = this.getTextSize();

        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    ;

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
            }
            this.setTextSize(trySize);
        }
    }

    ;

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }
}

