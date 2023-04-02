import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  lines: (string | null)[];

  constructor() {
    // TODO: Read from disclaimer.txt
    this.lines = [
      "This webshop weas created with Spring Boot + Angular",
      "Made by: Zsombor Toreky",
      new DOMParser().parseFromString("All rights reserved &copy", "text/html").body.textContent
    ];
  }
}
