import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Order} from '../model/order.model';

@Injectable()
export class OrderService {
    constructor(private http: HttpClient) {
    }

    baseUrl: string = 'http://localhost:9000/order-api/v1/orders';

    getOrders() {
        return this.http.get<Order[]>(this.baseUrl);
    }

    placeOrder(order: Order) {
        return this.http.post(this.baseUrl, order);
    }
}
