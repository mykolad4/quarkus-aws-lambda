package me.molka.lambda.data;

public interface ModifierColumns {
    String MERCHANT_NAME_COL = "merchant";
    String ID_COL = "id";

//    todo: try to rename to 'name'. 'NAME' - is reserved keyword
    String NAME_COL = "nameCol";
    String COST_COL = "cost";
    String AT_LEAST_COL = "atLeast";
    String GROUP_AT_LEAST_COL = "groupAtLeast";
    String AT_MOST_COL = "atMost";
    String GROUP_AT_MOST_COL = "groupAtMost";
    String IS_DEFAULT_COL = "isDefault";
    String IS_HIDDEN_COL = "isHidden";
}
