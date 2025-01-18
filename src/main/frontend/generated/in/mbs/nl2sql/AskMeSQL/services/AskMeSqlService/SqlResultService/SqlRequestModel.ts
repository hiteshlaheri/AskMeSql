import { _getPropertyModel as _getPropertyModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, ObjectModel as ObjectModel_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import type SqlRequest_1 from "./SqlRequest.js";
class SqlRequestModel<T extends SqlRequest_1 = SqlRequest_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(SqlRequestModel);
    get sqlquery(): StringModel_1 {
        return this[_getPropertyModel_1]("sqlquery", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
}
export default SqlRequestModel;
