import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";

import {OrderRouting} from "./order.routing";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {ListOrderComponent} from './views/list-order/list-order.component';
import {PlaceOrderComponent} from './views/place-order/place-order.component';
import {OrderService} from "./service/order.service";
import {CustomerService} from "../customer/service/customer.service";

@NgModule({
    declarations: [
        ListOrderComponent,
        PlaceOrderComponent,
    ],
    imports: [
        CommonModule,
        OrderRouting,
        ReactiveFormsModule,
        HttpClientModule,
    ],
    providers: [
        OrderService,
        CustomerService
    ]
})
export class OrderModule {
}