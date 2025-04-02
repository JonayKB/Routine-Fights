export type UserIn = {
  id: string;
  username: string;
  email: string;
  password: string;
  nationality: string;
  phoneNumber: string;
  image: "null.jpg";
};

export type UserOut = {
  id: string;
  username: string;
  email: string;
  nationality: string;
  phoneNumber: string;
  image: string;
  createdAt: string;
  followers: number;
  following: number;
};

export type Followers = {
  id: string;
  username: string;
  nationality: string;
  image: string;
  createdAt: string;
  followers: number;
  following: number;
};
