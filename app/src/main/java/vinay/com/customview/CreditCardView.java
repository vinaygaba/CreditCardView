package vinay.com.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by vgaba on 4/28/2015.
 */
public class CreditCardView extends RelativeLayout {

    String mCardNumber,mExpiryDate;

    public CreditCardView(Context context) {
        super(context);
    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CreditCardView,
                0, 0);

        try {
            mCardNumber = a.getString(R.styleable.CreditCardView_cardNumber);
            mExpiryDate = a.getString(R.styleable.CreditCardView_expiryDate);
        } finally {
            a.recycle();
        }
    }

    public String getCardNumber(){

        return mCardNumber;
    }

    public void setCardNumber(String cardNumber){
        mCardNumber = cardNumber;
        invalidate();
        requestLayout();
    }

    public String getExpiryDate(){

        return mExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        mExpiryDate = expiryDate;
        invalidate();
        requestLayout();
    }

}
