export class Customer {

    id: number;
    name: string;
    email: string;
    mobile: string;
    notificationEmail: boolean;
    notificationText: boolean;
    address: Address;
}

export class Address {

    street: string;
    city: string;
    number: string;
    country: string;
    zipCode: string;
}
