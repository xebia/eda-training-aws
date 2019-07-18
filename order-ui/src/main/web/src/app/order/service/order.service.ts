import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Order} from '../model/order.model';
import {VersionAwareService} from "../../shared/version-aware-service";

@Injectable()
export class OrderService extends VersionAwareService {
    constructor(private http: HttpClient) {
        super();
    }

    baseUrl: string = 'http://localhost:9000/order-api';

    getOrders() {
        return this.http.get<Order[]>(this.versionedUrl(this.baseUrl, 'orders'));
    }

    placeOrder(order: Order) {
        return this.http.post(this.versionedUrl(this.baseUrl, 'orders'), order);
    }
}
