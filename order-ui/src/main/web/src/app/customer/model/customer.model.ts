import {Address} from "./address.model";

export class Customer {
    id: number;
    name: string;
    mobile: string;
    email: string;
    address: Address;
    notificationEmail: boolean;
    notificationText: boolean;
}
