package com.vinaygaba.creditcardview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinaygaba.creditcardview.util.AndroidUtils;

import java.util.regex.Pattern;

/**
 * Created by vgaba on 4/28/2015.
 */
public class CreditCardView extends RelativeLayout{

    private static int CARD_FRONT = 0;
    private static int CARD_BACK = 1;

    private String mCardNumber = "";
    private String mCardName = "";
    private String mExpiryDate = "";
    private int mCardNumberTextColor = Color.WHITE;
    private int mCardNumberFormat = 0;
    private int mCardNameTextColor = Color.WHITE;
    private int mExpiryDateTextColor = Color.WHITE;
    private int mValidTillTextColor = Color.WHITE;
    private int mType = 0;
    private int mBrandLogo;
    private int mBrandLogoPosition = 1;
    private int cardSide = CARD_FRONT;
    private boolean mPutChip = false;
    private boolean mIsEditable=false;
    private int mHintTextColor = Color.WHITE;
    private boolean mIsFlippable = true;
    private Typeface creditCardTypeFace;
    private ImageButton mFlipBtn;
    private EditText cardNumber;
    private EditText cardName;
    private EditText expiryDate;
    private TextView validTill;
    private ImageView type;
    ImageView brandLogo;
    ImageView chip;



    public CreditCardView(Context context) {
        super(context);
        init();

    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadAttributes(attrs);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.creditcardview, this, true);


        if(!isInEditMode()) {
            // Font path
            String fontPath = "fonts/halter.ttf";
            // Loading Font Face
            creditCardTypeFace = Typeface.createFromAsset(getContext().getAssets(), fontPath);

        }
        cardNumber = (EditText)getChildAt(0);

        cardName = (EditText)getChildAt(1);

        type = (ImageView)getChildAt(2);

        brandLogo = (ImageView)getChildAt(3);

        chip = (ImageView)getChildAt(4);

        validTill = (TextView)getChildAt(5);

        expiryDate = (EditText)getChildAt(6);

        mFlipBtn = (ImageButton)getChildAt(7);
    }

    private void loadAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CreditCardView,
                0, 0);

        try {
            mCardNumber = a.getString(R.styleable.CreditCardView_cardNumber);
            mCardName = a.getString(R.styleable.CreditCardView_cardName);
            mExpiryDate = a.getString(R.styleable.CreditCardView_expiryDate);
            mCardNumberTextColor = a.getColor(R.styleable.CreditCardView_cardNumberTextColor, Color.WHITE);
            mCardNumberFormat = a.getInt(R.styleable.CreditCardView_cardNumberFormat, 0);
            mCardNameTextColor = a.getColor(R.styleable.CreditCardView_cardNumberTextColor, Color.WHITE);
            mExpiryDateTextColor = a.getColor(R.styleable.CreditCardView_expiryDateTextColor, Color.WHITE);
            mValidTillTextColor = a.getColor(R.styleable.CreditCardView_validTillTextColor, Color.WHITE);
            mType = a.getInt(R.styleable.CreditCardView_type, 0);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView_brandLogo, 0);
          //  mBrandLogoPosition = a.getInt(R.styleable.CreditCardView_brandLogoPosition, 1);
            mPutChip = a.getBoolean(R.styleable.CreditCardView_putChip, false);
            mIsEditable = a.getBoolean(R.styleable.CreditCardView_isEditable,false);
            mHintTextColor = a.getColor(R.styleable.CreditCardView_hintTextColor, Color.WHITE);
            mIsFlippable = a.getBoolean(R.styleable.CreditCardView_isFlippable, mIsFlippable);
        } finally {
            a.recycle();
        }

        //Set default background
        if(getBackground()==null){
            setBackgroundResource(R.drawable.cardbackground_sky);
        }

        if(!mIsEditable){

            cardNumber.setEnabled(false);
            cardName.setEnabled(false);
            expiryDate.setEnabled(false);
        }
        else{
            cardNumber.setHint(R.string.card_number_hint);
            cardNumber.setHintTextColor(mHintTextColor);

            cardName.setHint(R.string.card_name_hint);
            cardName.setHintTextColor(mHintTextColor);

            expiryDate.setHint(R.string.expiry_date_hint);
            expiryDate.setHintTextColor(mHintTextColor);
        }

        if(mCardNumber!= null)
            cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));
        cardNumber.setTextColor(mCardNumberTextColor);
        if(!isInEditMode()) {
            cardNumber.setTypeface(creditCardTypeFace);
        }

        if(mCardName!= null)
            cardName.setText(mCardName.toUpperCase());
        cardName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cardName.setTextColor(mCardNumberTextColor);
        if(!isInEditMode()) {
            cardName.setTypeface(creditCardTypeFace);
        }

        //Set the appropriate logo based on the type of card
        type.setBackgroundResource(getLogo(mType));

        //Set Brand Logo Position
      /*  RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)brandLogo.getLayoutParams();
        if(mBrandLogoPosition==0){
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        else if(mBrandLogoPosition==1){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }*/

        //If background logo attribute is present, set it
        if(mBrandLogo != 0) {
            brandLogo.setBackgroundResource(mBrandLogo);
           // brandLogo.setLayoutParams(params);
        }

        //If putChip attribute is present, change the visibility of the view and display it
        if(mPutChip){
            chip = (ImageView)getChildAt(4);
            chip.setVisibility(View.VISIBLE);
        }

        if(mExpiryDate!= null)
            expiryDate.setText(mExpiryDate);
        expiryDate.setTextColor(mExpiryDateTextColor);
        if(!isInEditMode()) {
            expiryDate.setTypeface(creditCardTypeFace);
        }

        validTill.setTextColor(mValidTillTextColor);

        mFlipBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flip();
            }
        });
        mFlipBtn.setEnabled(mIsFlippable);

        cardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mType =4;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                mCardNumber = cardNumber.getText().toString().replaceAll("\\s+","");
               /* if(mCardNumber.length()>12) {
                    cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));
                    if (mType == 4) {
                        type.setBackgroundResource(getLogo(mType));
                    }
                }*/
            }


        });

        cardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mCardNumber.length() > 12) {
                        cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));
                        if (mType == 4) {
                            type.setBackgroundResource(getLogo(mType));
                        }
                    }
                }
            }
        });


        cardName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                mCardName = cardName.getText().toString().toUpperCase();

            }

        });

        expiryDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                mExpiryDate = expiryDate.getText().toString();

            }

        });

    }

    public boolean isFlippable(){
        return mIsFlippable;
    }

    public void setIsFlippable(boolean flippable){
        mIsFlippable = flippable;
        if(mIsFlippable){
            mFlipBtn.setVisibility(View.VISIBLE);
        } else {
            mFlipBtn.setVisibility(View.INVISIBLE);
        }
        mFlipBtn.setEnabled(mIsFlippable);
    }

    public void flip(){
        if(mIsFlippable){
            if(AndroidUtils.icsOrBetter()){
                if(cardSide == CARD_FRONT){
                    rotateInToBack();
                } else if(cardSide == CARD_BACK){
                    rotateInToFront();
                }
            } else {
                if(cardSide == CARD_FRONT){
                    rotateInToBackBeforeEleven();
                } else if(cardSide == CARD_BACK){
                    rotateInToFrontBeforeEleven();
                }
            }

        }
    }

    private void showFrontView(){
        cardNumber.setVisibility(View.VISIBLE);
        cardName.setVisibility(View.VISIBLE);
        type.setVisibility(View.VISIBLE);
        brandLogo.setVisibility(View.VISIBLE);
        chip.setVisibility(View.VISIBLE);
        validTill.setVisibility(View.VISIBLE);
        expiryDate.setVisibility(View.VISIBLE);
    }

    private void hideFrontView(){
        cardNumber.setVisibility(View.GONE);
        cardName.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        brandLogo.setVisibility(View.GONE);
        chip.setVisibility(View.GONE);
        validTill.setVisibility(View.GONE);
        expiryDate.setVisibility(View.GONE);
    }

    public String getCardNumber(){

        return mCardNumber;
    }

    public void setCardNumber(String cardNumber){
        mCardNumber = cardNumber.replaceAll("\\s+","");
        invalidate();
        requestLayout();
    }

    public String getCardName(){

        return mCardName;
    }

    public void setCardName(String cardName){
        mCardName = cardName.toUpperCase();
        invalidate();
        requestLayout();
    }

    public int getCardNumberTextColor(){

        return mCardNumberTextColor;
    }

    public void setCardNumberTextColor(int cardNumberTextColor){
        mCardNumberTextColor = cardNumberTextColor;
        invalidate();
        requestLayout();
    }

    public int getCardNumberFormat(){

        return mCardNumberFormat;
    }

    public void setCardNumberFormat(int cardNumberFormat){
        mCardNumberFormat = cardNumberFormat;
        invalidate();
        requestLayout();
    }

    public int getCardNameTextColor(){

        return mCardNameTextColor;
    }

    public void setCardNameTextColor(int cardNameTextColor){
        mCardNameTextColor = cardNameTextColor;
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

    public int getExpiryDateTextColor(){

        return mExpiryDateTextColor;
    }

    public void setExpiryDateTextColor(int expiryDateTextColor){
        mExpiryDateTextColor = expiryDateTextColor;
        invalidate();
        requestLayout();
    }

    public int getValidTillTextColor(){

        return mValidTillTextColor;
    }

    public void setValidTillTextColor(int validTillTextColor){
        mValidTillTextColor = validTillTextColor;
        invalidate();
        requestLayout();
    }



    public int getType(){

        return mType;
    }

    public void setType(int type){

        mType = type;
        invalidate();
        requestLayout();
    }

    public boolean getIsEditable(){

        return mIsEditable;
    }

    public void setIsEditable(boolean isEditable){

        mIsEditable = isEditable;
        invalidate();
        requestLayout();
    }

    public int getHintTextColor(){

        return mHintTextColor;
    }

    public void setHintTextColor(int hintTextColor){
        mHintTextColor = hintTextColor;
        invalidate();
        requestLayout();
    }

    public int getBrandLogo(){

        return mBrandLogo;
    }

    public void setBrandLogo(int brandLogo){

        mBrandLogo = brandLogo;
        invalidate();
        requestLayout();
    }

    public int getBrandLogoPosition(){

        return mBrandLogo;
    }

    public void setBrandLogoposition(int brandLogoPosition){

        mBrandLogoPosition = brandLogoPosition;
        invalidate();
        requestLayout();
    }

    public int getLogo(int type){

        switch(type){
            case 0: return R.drawable.visa;


            case 1: return R.drawable.mastercard;


            case 2: return R.drawable.amex;

            case 3: return R.drawable.discover;

            case 4: return findCardType();

        }

        return 0;
    }

    public void putChip(){
        mPutChip = true;
        invalidate();
        requestLayout();
    }

    public String checkCardNumberFormat(String cardNumber){
        Log.e("Card Number",cardNumber);
        if(getCardNumberFormat()==1){

            cardNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4,cardNumber.length());
        }
        else if(getCardNumberFormat()==2){

            cardNumber = cardNumber.substring(cardNumber.length() - 4,cardNumber.length());
        }

        else if(getCardNumberFormat()==3){

            cardNumber = "**** **** **** ****";
        }

        return cardNumber;
    }

    public int findCardType(){

        int type = 0;
        if(cardNumber.length()>0) {
            String cardNumber = getCardNumber();
            cardNumber = cardNumber.replaceAll("\\s+", "");

            if (Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$^5[1-5][0-9]{14}$").matcher(cardNumber).matches())
                type = 0;
            else if (Pattern.compile("^5[1-5][0-9]{14}$").matcher(cardNumber).matches())
                type = 1;
            else if (Pattern.compile("^3[47][0-9]{13}$").matcher(cardNumber).matches())
                type = 2;
            else if (Pattern.compile("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$").matcher(cardNumber).matches())
                type = 3;
            else
                type = 0;
        }
        setType(type);

        return getLogo(type);
    }

    public String addSpaceToCardNumber(String cardNumber){

        if(cardNumber.length()%4 !=0){

            return cardNumber;
        }
        else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < cardNumber.length(); i++) {
                if (i % 4 == 0 && i != 0 && i!= cardNumber.length()-1) {
                    result.append(" ");
                }

                result.append(cardNumber.charAt(i));
            }

            return result.toString();
        }
    }

    @TargetApi(11)
    private void rotateInToBack(){
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator rotateIn = ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final ObjectAnimator hideFrontView = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(300);
        hideFrontView.setDuration(1);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateOutToBack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.play(hideFrontView).after(rotateIn);
        set.start();
    }

    @TargetApi(11)
    private void rotateInToFront(){
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator rotateIn = ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final ObjectAnimator hideBackView = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setDuration(300);
        hideBackView.setDuration(1);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateOutToFront();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.play(hideBackView).after(rotateIn);
        set.start();
    }

    @TargetApi(11)
    private void rotateOutToBack(){
        hideFrontView();
        CreditCardView.this.setRotationY(-90);
        setBackgroundResource(R.drawable.cardbackgroundback_default);
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator flipView = ObjectAnimator.ofInt(CreditCardView.this, "rotationY", 90, -90);
        final ObjectAnimator rotateOut = ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        final ObjectAnimator showBackView = ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        flipView.setDuration(0);
        showBackView.setDuration(1);
        rotateOut.setDuration(300);
        showBackView.setStartDelay(150);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //Do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardSide = CARD_BACK;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Do nothing
            }
        });
        set.play(flipView).with(showBackView).before(rotateOut);
        set.start();
    }

    @TargetApi(11)
    private void rotateOutToFront(){
        showFrontView();
        CreditCardView.this.setRotationY(-90);
        setBackgroundResource(R.drawable.cardbackground_sky);
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator flipView = ObjectAnimator.ofInt(CreditCardView.this, "rotationY", 90, -90);
        final ObjectAnimator rotateOut = ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        final ObjectAnimator showFrontView = ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        showFrontView.setDuration(1);
        rotateOut.setDuration(300);
        showFrontView.setStartDelay(150);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //Do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardSide = CARD_FRONT;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Do nothing
            }
        });
        set.play(flipView).with(showFrontView).before(rotateOut);
        set.start();
    }

    private void rotateInToBackBeforeEleven(){
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        final com.nineoldandroids.animation.ObjectAnimator rotateIn = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final com.nineoldandroids.animation.ObjectAnimator hideFrontView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(300);
        hideFrontView.setDuration(1);
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                rotateOutToBackBeforeEleven();
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(hideFrontView).after(rotateIn);
        set.start();
    }

    private void rotateInToFrontBeforeEleven(){
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        final com.nineoldandroids.animation.ObjectAnimator rotateIn = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final com.nineoldandroids.animation.ObjectAnimator hideBackView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setInterpolator( new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(300);
        hideBackView.setDuration(1);
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                rotateOutToFrontBeforeEleven();
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(hideBackView).after(rotateIn);
        set.start();
    }

    private void rotateOutToBackBeforeEleven(){
        hideFrontView();
        setBackgroundResource(R.drawable.cardbackgroundback_default);
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        com.nineoldandroids.animation.ObjectAnimator flip = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", 90, -90);
        com.nineoldandroids.animation.ObjectAnimator rotateOut = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        com.nineoldandroids.animation.ObjectAnimator showBackView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        flip.setDuration(0);
        showBackView.setDuration(1);
        rotateOut.setDuration(300);
        showBackView.setStartDelay(150);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                cardSide = CARD_BACK;
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(flip).with(showBackView).before(rotateOut);
        set.start();
    }

    private void rotateOutToFrontBeforeEleven(){
        showFrontView();
        setBackgroundResource(R.drawable.cardbackground_sky);
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        com.nineoldandroids.animation.ObjectAnimator flip = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", 90, -90);
        com.nineoldandroids.animation.ObjectAnimator rotateOut = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        com.nineoldandroids.animation.ObjectAnimator showFrontView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        showFrontView.setDuration(1);
        rotateOut.setDuration(300);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        showFrontView.setStartDelay(150);
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                cardSide = CARD_FRONT;
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(flip).with(showFrontView).with(rotateOut);
        set.start();
    }
}

