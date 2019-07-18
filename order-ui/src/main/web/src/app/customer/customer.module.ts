import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";

import {ListCustomerComponent} from "./views/list-customer/list-customer.component";
import {AddCustomerComponent} from "./views/add-customer/add-customer.component";
import {EditCustomerComponent} from "./views/edit-customer/edit-customer.component";
import {CustomerService} from "./service/customer.service";
import {CustomerRouting} from "./customer.routing";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
    declarations: [
        ListCustomerComponent,
        AddCustomerComponent,
        EditCustomerComponent
    ],
    imports: [
        CommonModule,
        CustomerRouting,
        ReactiveFormsModule,
        HttpClientModule,
    ],
    providers: [CustomerService],
})
export class CustomerModule {
}