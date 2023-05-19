import { CreditCardResponse } from "../interfaces/creditCardResponse";
import { Product } from "../interfaces/product";
import { IUser } from "../interfaces/user";

export class User implements IUser {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    sellingProducts: Array<Product>;
    creditCards: Array<CreditCardResponse>;
    role?: string;
    profilePictureUrl: string | undefined;
    readonly defaultProfilePictureUrl: string = "assets/img/profile_picture_default.PNG";

    constructor(id: number, firstName: string, lastName: string, email: string, 
        sellingProducts: Array<Product>, creditCards: Array<CreditCardResponse>, profilePictureUrl?: string) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sellingProducts = sellingProducts;
        this.creditCards = creditCards;
        this.profilePictureUrl = (profilePictureUrl ? profilePictureUrl : this.defaultProfilePictureUrl);
    }
}