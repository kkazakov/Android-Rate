Android-Rate
============

This project was forked from https://github.com/hotchemi/Android-Rate

The main difference is the possibility to set custom layout, without depending on the custom buttons.

You can do something like:

```java
    .setViewLayout(R.layout.rate_popup)
    .setOkButtonId(R.id.popup_button_1)
    .setLaterButtonId(R.id.popup_button_2)
    .setNoButtonId(R.id.popup_button_3)
```

and the result will be the same, but you can, for example, have vertical buttons.
