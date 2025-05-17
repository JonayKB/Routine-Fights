export type Comment = {
  createdAt: string;
  id: string;
  message: string;
  user: {
    image: string;
    email: string;
    username: string;
  };
};
