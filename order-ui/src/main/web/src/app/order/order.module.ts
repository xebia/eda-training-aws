import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";

import {OrderRouting} from "./order.routing";
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {ListOrderComponent} from './views/list-order/list-order.component';

@NgModule({
    declarations: [
        ListOrderComponent
    ],
    imports: [
        CommonModule,
        OrderRouting,
        ReactiveFormsModule,
        HttpClientModule,
    ]
})
export class OrderModule {
}