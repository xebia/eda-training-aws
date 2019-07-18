import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {AppRouting} from "./app.routing";
import {CustomerModule} from "./customer/customer.module";
import {OrderModule} from "./order/order.module";

@NgModule({
    declarations: [
        AppComponent,
    ],
    imports: [
        BrowserModule,
        AppRouting,
        CustomerModule,
        OrderModule,
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
