export type Post = {
  id: string;
  streak: number;
  comments: number;
  image: string;
  likes: number;
  isLiked: boolean;
  updatedAt: string;
  createdAt: string;
  user: {
    email: string;
    image: string;
  };
};
