export type UserIn = {
  username: string;
  email: string;
  password: string;
  nationality: string;
  phoneNumber: string;
  image: string;
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
  isFollowing: boolean;
};

export type Followers = {
  id: string;
  username: string;
  image: string;
  followers: number;
  following: number;
  isFollowing: boolean;
  email: string;
};
