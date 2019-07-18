import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit  {
    title = 'app';

    ngOnInit() {
        const apiVersion = localStorage.getItem("apiVersion");
        if (!apiVersion) {
            localStorage.setItem("apiVersion", "v1");
        }
    }

    changeVersion(version: string) {
        localStorage.setItem("apiVersion", version);
        window.location.reload();
    }

    getVersion() {
        return localStorage.getItem("apiVersion");
    }
}
