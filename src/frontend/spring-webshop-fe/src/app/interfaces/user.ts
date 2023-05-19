import { CreditCardResponse } from "./creditCardResponse"
import { Product } from "./product"

export interface IUser {
    id: number,
    firstName: string,
    lastName: string,
    email: string,
    sellingProducts: Array<Product>,
    creditCards: Array<CreditCardResponse>,
    role?: string,
    profilePictureUrl?: string
};