import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OrderService} from "../../service/order.service";
import {Router} from "@angular/router";
import {CustomerService} from "../../../customer/service/customer.service";
import {Customer} from "../../../customer/model/customer.model";
import {Order, OrderState} from "../../model/order.model";
import {OrderLine} from "../../model/order-line.model";

@Component({
    selector: 'app-place-order',
    templateUrl: './place-order.component.html',
    styleUrls: ['./place-order.component.css']
})
export class PlaceOrderComponent implements OnInit {

    addForm: FormGroup;
    customers: Customer[];
    products = [
        { productId: 1, productName: "Fancy book", priceCents: 150 },
        { productId: 2, productName: "Nifty Swiss Army knife", priceCents: 1000 },
        { productId: 3, productName: "Fast computer", priceCents: 10000 },
    ];

    constructor(private formBuilder: FormBuilder, private router: Router, private orderService: OrderService, private customerService: CustomerService) {
    }

    ngOnInit() {
        this.addForm = this.formBuilder.group({
            id: [],
            customerId: ['', Validators.required],
            line1: ['', Validators.required],
            line1Count: ['', Validators.required],
            line2: [''],
            line2Count: [''],
            line3: [''],
            line3Count: [''],
        });

        this.customerService.getCustomers()
            .subscribe(data => {
                this.customers = data;
            });
    }

    onSubmit() {
        if (this.addForm.valid) {
            const formValue = this.addForm.value;
            const orderLines = [
                this.toOrderLine(formValue.line1, formValue.line1Count)
            ];

            if (formValue.line2 !== '') {
                orderLines.push(this.toOrderLine(formValue.line2, formValue.line2Count));
            }

            if (formValue.line3 !== '') {
                orderLines.push(this.toOrderLine(formValue.line3, formValue.line3Count));
            }

            const order = new Order(
                null,
                formValue.customerId,
                OrderState.INITIATED,
                new Date(),
                this.findCustomer(formValue.customerId).address,
                orderLines
            );

            this.orderService.placeOrder(order)
                .subscribe(data => {
                    this.router.navigate(['order']);
                });
        }
    }

    private toOrderLine(productId: number, itemCount: number): OrderLine {
        console.log(this.products);
        const product = this.products.find(p => p.productId == productId);
        if (product) {
            return new OrderLine(
                null,
                product.productId,
                product.productName,
                itemCount,
                product.priceCents
            );
        }
        return null;
    }

    private findCustomer(customerId: number): Customer {
        console.log(this.customers);
        return this.customers.find(c => c.id == customerId);
    }
}
