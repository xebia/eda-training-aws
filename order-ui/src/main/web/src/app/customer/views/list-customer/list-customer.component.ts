import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CustomerService} from "../../service/customer.service";
import {Customer} from "../../model/customer.model";

@Component({
    selector: 'app-list-customer',
    templateUrl: './list-customer.component.html',
    styleUrls: ['./list-customer.component.css']
})
export class ListCustomerComponent implements OnInit {

    customers: Customer[];

    constructor(private router: Router, private customerService: CustomerService) {
    }

    ngOnInit() {
        this.customerService.getCustomers()
            .subscribe(data => {
                this.customers = data.sort((a, b) => a.id - b.id);
            });
    }

    deleteCustomer(customer: Customer): void {
        this.customerService.deleteCustomer(customer.id)
            .subscribe(data => {
                this.customers = this.customers.filter(u => u !== customer);
            })
    };

    editCustomer(customer: Customer): void {
        localStorage.removeItem("editCustomerId");
        localStorage.setItem("editCustomerId", customer.id.toString());
        this.router.navigate(['customer/edit']);
    };

    addCustomer(): void {
        this.router.navigate(['customer/add']);
    };
}
