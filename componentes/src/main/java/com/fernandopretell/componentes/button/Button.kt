package com.fernandopretell.componentes.button

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import biz.belcorp.mobile.components.core.extensions.setSafeOnClickListener
import biz.belcorp.mobile.components.core.extensions.setSvgColor
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import com.fernandopretell.componentes.R
import kotlinx.android.synthetic.main.button.view.*

class Button: LinearLayout {

    // VAR

    val typefaceRegular = ResourcesCompat.getFont(context, R.font.lato_regular)
    val typefaceBold = ResourcesCompat.getFont(context, R.font.lato_bold)

    var textBold: Boolean = true
    var textSize: Float = context.resources.getDimension(R.dimen.btn_default_text_size)
    var paddingHorizontal = context.resources.getDimensionPixelSize(R.dimen.btn_margin_horizontal)
    val paddingVertical = context.resources.getDimensionPixelSize(R.dimen.btn_margin_vertical)
    var minHeightBtn: Float = context.resources.getDimension(R.dimen.btn_min_height)

    var buttonClickListener : OnClickListener? = null
    var iconLeft: Int = -1
    var iconRight: Int = -1
    var color: Int = ContextCompat.getColor(context, R.color.black)
    var rippleColor: Int = ContextCompat.getColor(context, R.color.gray_1)
    var textColor: Int = ContextCompat.getColor(context, R.color.white)
    var textBtn: String? = context.resources.getString(R.string.btn_name)
    var borderColor: Int = -1
    var borderRadius: Float = context.resources.getDimension(R.dimen.btn_border_radius)
    var borderSize: Int = context.resources.getDimensionPixelSize(R.dimen.btn_border_size)
    var outline: Boolean = false
    var disabled: Boolean = false
    var disabledOutline: Boolean = false

    private var colorDisable: Int = ContextCompat.getColor(context, R.color.gray_3)
    var textWithUnderline: Boolean = false
    private var textColorDisable: Int = ContextCompat.getColor(context, R.color.gray_4)
    private var mAttrs: AttributeSet? = null

    var stylesHelper: StylesHelper =
        StylesHelper(context)

    // CONSTRUCTOR

    constructor(context: Context): super(context) {
        inflate(context, R.layout.button, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflate(context, R.layout.button, this)
        mAttrs = attrs
        setupAttrs(context, attrs)
        setupUI()
    }

    // METHOD PRIVATE

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Button, 0, 0)
        try {
            color = ta.getInt(R.styleable.Button_btn_color, color)
            rippleColor = ta.getInt(R.styleable.Button_btn_ripple_color, rippleColor)
            iconLeft = ta.getResourceId(R.styleable.Button_btn_icon_left, iconLeft)
            iconRight = ta.getResourceId(R.styleable.Button_btn_icon_right, iconRight)
            borderRadius = ta.getDimension(R.styleable.Button_btn_border_radius, borderRadius)
            borderSize = ta.getDimensionPixelSize(R.styleable.Button_btn_border_size, borderSize)
            borderColor = ta.getColor(R.styleable.Button_btn_border_color, borderColor)
            outline = ta.getBoolean(R.styleable.Button_btn_outline, outline)
            disabled = ta.getBoolean(R.styleable.Button_btn_disable, disabled)
            textBtn = ta.getString(R.styleable.Button_btn_text)
            textColor = ta.getColor(R.styleable.Button_btn_text_color, textColor)
            textBold = ta.getBoolean(R.styleable.Button_btn_text_bold, textBold)
            textSize = ta.getDimension(R.styleable.Button_btn_text_size, textSize)
            textWithUnderline = ta.getBoolean(R.styleable.Button_btn_text_underline, textWithUnderline)
            minHeightBtn = ta.getDimension(R.styleable.Button_btn_min_height, minHeightBtn)

        } finally {
            ta.recycle()
        }
    }

    fun setupUI() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER
        setPadding(paddingHorizontal, 0, paddingHorizontal, 0)

        this.minimumHeight = minHeightBtn.toInt()
        setupText()
        setupContainerButton()

        this.setOnLongClickListener(null)
        this.setSafeOnClickListener {
            buttonClickListener?.onClick(it)
        }

    }

    fun updateAttributes(color: Int?, iconLeft: Int?, iconRight: Int?, borderRadius: Float?,
                         borderSize: Int?, outline: Boolean?, disabled: Boolean?, textBtn: String?) {
        this.color = color ?: this.color
        this.iconLeft = iconLeft ?: this.iconLeft
        this.iconRight = iconRight ?: this.iconRight
        this.borderRadius = borderRadius ?: this.borderRadius
        this.borderSize = borderSize ?: this.borderSize
        this.outline = outline ?: this.outline
        this.disabled = disabled ?: this.disabled
        this.textBtn = textBtn ?: this.textBtn

        setupUI()
    }

    private fun setupText() {
        var textStyle = typefaceRegular

        if (textBold)
            textStyle = typefaceBold

        if (textWithUnderline) {
            textButton.paintFlags = textButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        val textColorStyle = if (disabled) {
            textColorDisable
        } else {
            textColor
        }

        stylesHelper.updateTextViewStyle(textButton, textStyle, textColorStyle, this.textSize)

        textBtn?.let {
            textButton.text = textBtn
        }
    }

    private fun setupContainerButton() {
        this.isEnabled = !disabled
        if (disabled) {
            if (outline) {
                setBackgroundOutlineDisable()
            } else {
                setBackgroundDisable()
            }
        } else {
            if (outline) {
                setBackgroundOutline()
            } else {
                setBackground()
            }
        }
    }

    private fun setBackgroundDisable() {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = borderRadius
        shape.setColor(colorDisable)

        setupIcon(textColorDisable)

        this.background = shape
    }

    private fun setBackgroundOutlineDisable() {
        if (borderColor == -1) {
            borderColor = color
        }

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = borderRadius
        shape.setStroke(borderSize, textColorDisable)
        shape.setColor(colorDisable)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.background = getBackgroundDrawable(rippleColor, shape)
        } else {
            this.background = shape
        }

        textButton.setTextColor(textColorDisable)

        setupIcon(textColorDisable)
    }

    private fun setBackground() {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = borderRadius
        shape.setColor(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.background = getBackgroundDrawable(rippleColor, shape)
        } else {
            this.background = shape
        }

        setupIcon(textColor)
    }

    private fun setBackgroundOutline() {
        if (borderColor == -1) {
            borderColor = color
        }

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = borderRadius
        shape.setStroke(borderSize.toInt(), borderColor)
        shape.setColor(Color.WHITE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.background = getBackgroundDrawable(rippleColor, shape)
        } else {
            this.background = shape
        }

        textButton.setTextColor(borderColor)

        setupIcon(borderColor)
    }

    fun setupIcon(color: Int) {

        if (iconLeft == -1) {
            iconButtonLeft.visibility = View.GONE
        } else {
            iconButtonLeft.visibility = View.VISIBLE
            iconButtonLeft.setImageDrawable(ContextCompat.getDrawable(context, iconLeft))
            iconButtonLeft.setSvgColor(color)
        }

        if (iconRight == -1) {
            iconButtonRight.visibility = View.GONE
        } else {
            iconButtonRight.visibility = View.VISIBLE
            iconButtonRight.setImageDrawable(ContextCompat.getDrawable(context, iconRight))
            iconButtonRight.setSvgColor(color)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBackgroundDrawable(pressedColor: Int, backgroundDrawable: Drawable): RippleDrawable {
        return RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null)
    }

    private fun getPressedState(pressedColor: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor))
    }

    // public method

    fun textWithUnderline(withUnderLine: Boolean) {
        textWithUnderline = withUnderLine
        setupText()
    }

    fun textColor(color: Int) {
        this.textColor = color
        setupText()
    }

    fun setText(text: String) {
        this.textBtn = text
        setupText()
    }

    fun addIconLeft(icon: Int?) {
        this.iconLeft = icon?: -1
        setupUI()
    }

    fun addIconRight(icon: Int?) {
        this.iconRight = icon?: -1
        setupUI()
    }

    fun addBackgroundColor(color: Int) {
        this.color = color
        setupUI()
    }

    fun addBackgroundColorResource(color: Int) {
        this.color = ContextCompat.getColor(context, color)
        setupUI()
    }

    fun addBorderSize(size: Int) {
        this.borderSize = size
        setupUI()
    }

    fun addBorderRadius(radius: Int) {
        this.borderRadius = radius.toFloat()
        setupUI()
    }

    fun addBorderColor(color: Int) {
        this.borderColor = color
        setupUI()
    }

    fun ilimitedMaxLines() {
        textButton.maxLines = Int.MAX_VALUE
        setupText()
    }

    fun maxLines(maxLines: Int) {
        textButton.maxLines = maxLines
        setupText()
    }

    fun isOutline(outline: Boolean) {
        this.outline = outline
        setupUI()
    }

    fun isDisable(disable: Boolean) {
        this.disabled = disable
        setupUI()
    }

    fun addColorDisable(color: Int) {
        this.colorDisable = color
        setupUI()
    }

    fun addTextColorDisable(color: Int) {
        this.textColorDisable = color
        setupUI()
    }

    fun resetStyleColorDisable() {
        this.colorDisable = ContextCompat.getColor(context, R.color.gray_3)
        this.textColorDisable = ContextCompat.getColor(context, R.color.gray_4)
        setupUI()
    }

    fun setButtonGravity(gravity: Int){
        this.gravity = gravity
    }

    // interface

    interface OnClickListener {
        fun onClick(view: View)
    }

}
