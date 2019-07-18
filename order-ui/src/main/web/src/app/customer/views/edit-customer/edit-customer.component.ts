import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import {Customer} from "../../model/customer.model";

@Component({
    selector: 'app-edit-customer',
    templateUrl: './edit-customer.component.html',
    styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit {

    customer: Customer;
    editForm: FormGroup;

    constructor(private formBuilder: FormBuilder, private router: Router, private userService: CustomerService) {
    }

    ngOnInit() {
        let userId = localStorage.getItem("editCustomerId");
        if (!userId) {
            return this.router.navigate(['customer']);
        }
        this.editForm = this.formBuilder.group({
            id: [],
            name: ['', Validators.required],
            email: ['', Validators.required],
        });
        this.userService.getCustomerById(+userId)
            .subscribe(data => {
                this.editForm.setValue(data);
            });
    }

    onSubmit() {
        this.userService.updateCustomer(this.editForm.value)
            .pipe(first())
            .subscribe(
                data => {
                    this.router.navigate(['customer']);
                },
                error => {
                    alert(error);
                });
    }

}
