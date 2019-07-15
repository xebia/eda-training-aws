import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../service/user.service";
import {first} from "rxjs/operators";
import {Router} from "@angular/router";

@Component({
    selector: 'app-add-user',
    templateUrl: './add-user.component.html',
    styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

    constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService) {
    }

    addForm: FormGroup;

    ngOnInit() {

        this.addForm = this.formBuilder.group({
            id: [],
            name: ['', Validators.required],
            email: ['', Validators.required],
            mobile: ['', Validators.required],
            notificationEmail: [''],
            notificationText: [''],
            address: this.formBuilder.group({
                street: ['', [Validators.required]],
                number: ['', [Validators.required]],
                zipCode: ['', [Validators.required]],
                city: ['', [Validators.required]],
                country: ['', [Validators.required]]
            })
        });

    }

    onSubmit() {
        this.userService.createUser(this.addForm.value)
            .subscribe(data => {
                this.router.navigate(['list-user']);
            });
    }

}
