import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CustomerService} from "../../service/customer.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-add-customer',
    templateUrl: './add-customer.component.html',
    styleUrls: ['./add-customer.component.css']
})
export class AddCustomerComponent implements OnInit {

    constructor(private formBuilder: FormBuilder, private router: Router, private customerService: CustomerService) {
    }

    addForm: FormGroup;

    ngOnInit() {
        this.addForm = this.formBuilder.group({
            id: [],
            name: ['', Validators.required],
            email: ['', Validators.required],
            mobile: ['', Validators.required],
            address: this.formBuilder.group({
                street: ['', Validators.required],
                number: ['', Validators.required],
                zipCode: ['', Validators.compose([
                    Validators.required,
                    Validators.maxLength(6)
                ])],
                city: ['', Validators.required],
                country: ['', Validators.required],
            }),
            notificationEmail: [''],
            notificationText: [''],
        });
    }

    onSubmit() {
        if (this.addForm.valid) {
            this.customerService.createCustomer(this.addForm.value)
                .subscribe(data => {
                    this.router.navigate(['customer']);
                });
        }
    }

}
