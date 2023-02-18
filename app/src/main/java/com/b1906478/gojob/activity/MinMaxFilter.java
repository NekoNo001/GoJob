package com.b1906478.gojob.activity;
import android.text.InputFilter ;
import android.text.Spanned ;

public class MinMaxFilter implements InputFilter {
    private float mIntMin , mIntMax ;

    public MinMaxFilter ( float minValue , float maxValue) {
        this . mIntMin = minValue ;
        this . mIntMax = maxValue ;
    }

    public MinMaxFilter (String minValue , String maxValue) {
        this . mIntMin = Float.parseFloat(minValue) ;
        this . mIntMax = Float.parseFloat(maxValue) ;
    }

    @Override
    public CharSequence filter (CharSequence source , int start , int end , Spanned dest ,int dstart , int dend) {
        try {
            float input = Float.parseFloat (dest.toString() + source.toString()) ;
            if (isInRange( mIntMin , mIntMax , input))
                return null;
        } catch (NumberFormatException e) {
            e.printStackTrace() ;
        }
        return "" ;
    }

    private boolean isInRange ( float a , float b , float c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a ;
    }
}
