import { _getPropertyModel as _getPropertyModel_1, ArrayModel as ArrayModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1 } from "@vaadin/hilla-lit-form";
import type SqlResponse_1 from "./SqlResponse.js";
class SqlResponseModel<T extends SqlResponse_1 = SqlResponse_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(SqlResponseModel);
    get response(): ArrayModel_1<ObjectModel_1<Record<string, unknown>>> {
        return this[_getPropertyModel_1]("response", (parent, key) => new ArrayModel_1(parent, key, true, (parent, key) => new ObjectModel_1(parent, key, true, { meta: { javaType: "java.util.Map" } }), { meta: { javaType: "java.util.List" } }));
    }
    get noofrows(): NumberModel_1 {
        return this[_getPropertyModel_1]("noofrows", (parent, key) => new NumberModel_1(parent, key, false, { meta: { javaType: "int" } }));
    }
}
export default SqlResponseModel;
