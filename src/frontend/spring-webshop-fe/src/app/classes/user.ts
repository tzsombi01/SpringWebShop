import { IUser } from "../interfaces/user";

export class User implements IUser {
    name: string;
    email: string;
    profilePictureUrl: string | undefined;
    readonly defaultProfilePictureUrl: string = "assets/img/profile_picture_default.PNG";

    constructor(name: string, email: string, profilePictureUrl?: string) {
        this.name = name;
        this.email = email;
        this.profilePictureUrl = (profilePictureUrl ? profilePictureUrl : this.defaultProfilePictureUrl);
    }
}