import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {OrderService} from "../../service/order.service";
import {Order} from "../../model/order.model";
import {OrderLine} from "../../model/order-line.model";
import {CustomerService} from "../../../customer/service/customer.service";
import {Customer} from "../../../customer/model/customer.model";

@Component({
    selector: 'app-list-order',
    templateUrl: './list-order.component.html',
    styleUrls: ['./list-order.component.css']
})
export class ListOrderComponent implements OnInit {

    orders: Order[];
    customers: Map<number, Customer> = new Map<number, Customer>();

    constructor(private router: Router, private orderService: OrderService, private customerService: CustomerService) {
    }

    ngOnInit() {
        this.orderService.getOrders().subscribe(orders => {
            this.orders = orders.sort((a, b) => a.id - b.id);
            orders.forEach(order => {
                this.customerService.getCustomerById(order.customerId).subscribe(customer => {
                    this.customers.set(order.customerId, customer);
                })
            })
        })
    }

    placeOrder(): void {
        this.router.navigate(['order/place']);
    };

    reduceOrderLines(lines: OrderLine[]) {
        return lines.reduce((acc, cur, idx) => `${acc}${idx > 0 ? ', ' : ''}${cur.productName} (${cur.itemCount})`, '')
    }

    getCustomerById(customerId: number) {
        return this.customers.get(customerId);
    }
}
