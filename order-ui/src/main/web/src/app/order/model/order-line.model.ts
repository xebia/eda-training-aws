export class OrderLine {
    constructor(public id: number,
                public productId: number,
                public productName: string,
                public itemCount: number,
                public priceCents: number) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.itemCount = itemCount;
        this.priceCents = priceCents;
    }
}