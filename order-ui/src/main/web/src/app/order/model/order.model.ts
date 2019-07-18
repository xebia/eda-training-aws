import {Address} from "../../customer/model/address.model";
import {OrderLine} from "./order-line.model";

export enum OrderState {
    INITIATED = "INITIATED",
    SHIPPED = "SHIPPED"
}

export class Order {
    constructor(public id: number,
                public customerId: number,
                public status: OrderState,
                public created: Date,
                public shippingAddress: Address,
                public lines: OrderLine[]) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.created = created;
        this.shippingAddress = shippingAddress;
        this.lines = lines;
    }
}